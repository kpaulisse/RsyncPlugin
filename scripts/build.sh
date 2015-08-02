#!/bin/bash -ex

mkdir -p export/plugins
mkdir -p export/features
mkdir -p export/updatesite

currentdir=$(perl -e 'use Cwd qw/abs_path getcwd/; print abs_path(getcwd())')
featuredir=$(mktemp -d)
version=$(grep ^pluginVersion= build.properties | cut -d= -f2)
tar -cpf - . | ( cd $featuredir && tar -xpf - )
cd $featuredir
perl -pi -e "s/\\\$\\{pluginVersion\\}/$version/g" $featuredir/feature.xml
perl -pi -e "s/\\\$\\{pluginVersion\\}/$version/g" $featuredir/site.xml
perl -pi -e "s/TIMESTAMP/time/ge" $featuredir/artifacts.xml
perl -pi -e "s/VERSION/$version/g" $featuredir/artifacts.xml
artifact_size=$(stat -c "%s" $featuredir/export/plugins/com.paulisse.eclipse.plugin.rsync_${version}.jar)
feature_size=$(stat -c "%s" $featuredir/export/features/RsyncPluginFeature_${version}.jar)
perl -pi -e "s%ARTIFACTSIZE%${artifact_size}%g" $featuredir/artifacts.xml
perl -pi -e "s%FEATURESIZE%${feature_size}%g" $featuredir/artifacts.xml
perl -pi -e "s/TIMESTAMP/time/ge" $featuredir/content.xml
perl -pi -e "s/VERSION/$version/g" $featuredir/content.xml
echo "basedir=$currentdir" >> $featuredir/build.properties
ant -buildfile build-feature.xml build.update.jar publish.bin.parts
jar cvf $featuredir/export/updatesite/artifacts.jar artifacts.xml
jar cvf $featuredir/export/updatesite/content.jar content.xml
cp -Rp $featuredir/export/plugins $featuredir/export/updatesite
cp -Rp $featuredir/export/features $featuredir/export/updatesite

cd $featuredir/export
rm -rf $currentdir/export
mkdir -p $currentdir/export
tar -cpvf - * | (cd $currentdir/export && tar -xpf -)
cd $currentdir
rm -rf $featuredir
