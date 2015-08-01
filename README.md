# RsyncPlugin - A light-weight Eclipse plugin to rsync changes

## Overview

This is a very light-weight plugin I wrote for Eclipse to invoke "rsync" when something is saved. I personally use
it to update a small remote web application for development purposes.

This plugin may be for you if:

* You already know how to use rsync and you think it's awesome

* Your source system (where you use Eclipse) is Linux or Mac

* You use rsync over SSH, and you can get to your target system by SSH

If you don't meet the criteria above, please use something more user-friendly, like:

* [Eclipse PTP](http://www.eclipse.org/ptp/)
* [Aptana Studio](http://www.aptana.com/)

## Limitations

* I've only tested this on Linux and Mac. Sorry, Windows users; I'm not sure if it'll work for you.

* Rsync is a very powerful tool, which makes it dangerous if misused.

    * You need to know some basics of rsync to use this effectively.  If you are not familiar with rsync, you
        should not use this plugin.

    * There's very little error checking or
        hinting around rsync, as this plugin is focused mostly on passing your commands directly through (with the
        assumption that re-implementing the plethora of options as a GUI would defeat the purpose of creating a
        light-weight plugin). Please be careful!

    * By default this plugin comes with rsync "dry run" enabled, which means it won't actually do anything.
        You have to configure the global preference to remove the "dry run" flag. If you don't know what the
        "dry run" flag to rsync is, you should not be using this plugin.

* This plugin uses and requires rsync over SSH. It does not handle non-encrypted protocols (such as rsh or
    rsync native), nor do I have plans to add any such non-encrypted protocols. It's also assuming public key
    authentication, although password authentication presents a pop-up that seems to work. If you are not
    going to be using rsync over SSH, you should not use this plugin.

* Upon each "build" (incremental or full) a full rsync will be performed. If you have lots of files in your project
    or a slow connection, this might be pretty slow. The alternatives noted above may be faster.

## Installation

I may make an update site later. For now you have to download the project and compile it in an Eclipse instance
with the plugin development tools installed.

* Clone repo into Eclipse as new project
* Choose File -> Export, then Plug-in Development -> Deployable plug-ins and fragments
* Select the project
* Select Destination as "Install into host. Repository:" and select default repository
* Click "Finish" button to do it

## Instructions

Once installed, there are three points of configuration.

### Preferences

This creates an entry under Preferences called "Remote Sync (rsync)". The options have reasonable defaults:

* **Path to rsync** (required): Full path to the "rsync" binary on your system.

* **rsync options** (required): Options to use on every run. The default is `-anvx` which:
    * `-a` Archive: preserve ownership, permissions, and other metadata; be recursive; etc.
    * `-n` Dry Run: just print out what would be done, but don't actually do anything
    * `-v` Verbose: be verbose
    * `-x` One file system: do not traverse file system boundaries

    Please note that I left the option to delete files on the target that don't exist on the source out of
    the defaults, because it's dangerous, but you probably want to use it. Please make sure you know you are
    doing if you enable it!

    Also, I put the `-n` option in there as the default, to force people to look at the preference page before
    this would have a chance of doing things to your remove system that you don't expect. For this plugin do
    to anything useful, you'll have to remove that.

* **rsync excludes** (optional): To exclude files, directories, or other patterns, list them 1 per line.

Please see the [rsync man page](https://download.samba.org/pub/rsync/rsync.html) for additional details.

### Project Properties

This creates an entry under Properties called "Remote Sync (rsync)". The options are as follows:

* **Server Name** (required): The FQDN or IP address you're going to synchronize to.

* **Username** (required): Your username on the remote server.

* **SSH Port** (optional): Port to use - if blank, defaults to 22.

* **SSH Options** (optional): Additional options for SSH, one per line (see below note).

* **Remote Path** (required): Remote path to synchronize to.

* **Exclude dirs/files** (optional): To exclude files, directories, or other patterns, list them 1 per line. These
    exclusions are added to the excludes from the preferences.

Further explanation of the SSH options:

Normally, when you run rsync over SSH, you have to specify the following flag:

        rsync ... -e ssh ...

Sometimes, you need to pass other options to SSH with the "-o" flag. One use case I could see for this would be to
specify an identity file other than the default. To do this you would:

        rsync ... -e "ssh -o IdentityFile=/home/user/.ssh/some_other_file" ...

To do this within the plugin, put one `-o` option per line in the properties screen, without the `-o`. For example:

        IdentityFile=/home/user/.ssh/some_other_file
        SomeOtherParameter=value
        SomeOtherOtherParameter=value2

For security reasons, only certain characters are allowed in values. If you run into a problem, have a look in the
InvokeRsync.java file. Note that the ssh (and hence rsync) will fail if you specify an invalid parameter name.

### Project Nature and Menus

Once you've configured the preferences, there is a menu added to the project's right-click menu for
"Remote Sync (rsync)".

If automatic synchronization is disabled for this project (which it will be by default), the menu option
"Enable Automatic Synchronization (rsync)" will activate it. This will immediately synchronize your project assuming
you've set up everything correctly. If you've never synchronized this project before, and the project is big,
this may take a long time.

If the plugin is enabled, "Disable Automatic Synchronization (rsync)" will disable the automatic synchronization
for the project.

Regardless of whether the automatic synchronization is enabled or disabled, "Synchronize now" forces an immediate run.
If you want to rsync on-demand only, just leave the automatic sync disabled and use this "Synchronize now" option
when you need it.

### Output

Output is visible within the Eclipse console.

