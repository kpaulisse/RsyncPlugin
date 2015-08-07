#!/bin/bash -ex

mkdir -p export/plugins
mkdir -p export/features
mkdir -p export/updatesite

currentdir=$(perl -e 'use Cwd qw/abs_path getcwd/; print abs_path(getcwd())')
featuredir=$(mktemp -d)
version=$(grep ^pluginVersion= build.properties | cut -d= -f2)
tar -cpf - . | ( cd $featuredir && tar -xpf - )

cd $featuredir
mv $featuredir/scripts/*.xml $featuredir
mv $featuredir/src/main/resources/*.xml $featuredir

perl -pi -e "s/\\\$\\{pluginVersion\\}/$version/g" $featuredir/*.xml
perl -pi -e "s/0\\.0\\.1/$version/g" $featuredir/META-INF/MANIFEST.MF
perl -pi -e "s/VERSION/$version/g" $featuredir/*.xml
echo "basedir=$currentdir" >> $featuredir/build.properties
ant -buildfile build-feature.xml build.update.jar publish.bin.parts

java -jar \
    /opt/eclipse/plugins/org.eclipse.equinox.launcher_1.3.0.v20140415-2008.jar \
    -application org.eclipse.equinox.p2.publisher.FeaturesAndBundlesPublisher \
    -metadataRepository file:/${featuredir}/repository \
    -artifactRepository file:/${featuredir}/repository \
    -source ${featuredir}/export \
    -configs gtk.linux.x86 \
    -publishArtifacts

java -jar \
    /opt/eclipse/plugins/org.eclipse.equinox.launcher_1.3.0.v20140415-2008.jar \
    -application org.eclipse.equinox.p2.publisher.CategoryPublisher \
    -metadataRepository file:/${featuredir}/repository \
    -categoryDefinition file:/${featuredir}/category.xml

perl -pi -e "s/<repository name='.*?'/<repository name='RsyncPlugin Repository'/g" ${featuredir}/repository/*.xml
perl -pi -e "s/<unit id='.*?category\.xml.all' version='.*?'>/<unit id='category.xml' version='$version'>/g" ${featuredir}/repository/*.xml
perl -pi -e "s/<provided namespace='org.eclipse.equinox.p2.iu' name='.*?category.xml.all' version='.*?'\/>/<provided namespace='org.eclipse.equinox.p2.iu' name='category.xml' version='$version'\/>/g" ${featuredir}/repository/*.xml

rm -rf $currentdir/export
mkdir -p $currentdir/export
tar -cpvf - repository plugins features | (cd $currentdir/export && tar -xpf -)
cd $currentdir
rm -rf $featuredir
