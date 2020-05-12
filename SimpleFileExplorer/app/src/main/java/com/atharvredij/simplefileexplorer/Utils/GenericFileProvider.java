package com.atharvredij.simplefileexplorer.Utils;

import android.support.v4.content.FileProvider;

// 1
// From Android 7.0 apps are not allowed to share a ‘file:///’ URI with an Intent,
// we would require that to open files when when the user taps on them.
// We can fix this behaviour by proving a FileProvider and
// then getting a shareable Uri for our ‘file:///’ path.

// 2
// Register GenericFileProvider in manifest.

// 3
// Then provider_paths.xml file in res/xml folder.
// It describes that we would like to share access to the External Storage
// at root folder (path=".") with the name external_files.

public class GenericFileProvider extends FileProvider {
}
