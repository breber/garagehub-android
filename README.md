## Publishing an Update

* Increment the `versionCode` attribute in the AndroidManifest.xml
* Update the `android:value` attribute of the `<meta-data android:name="VERSION">` tag to match the `versionCode` value updated in step 1
* Update the `versionName` attribute to the next logical version (x.x.x convention)
* Export a signed APK from Eclipse using the key that is in the Dropbox folder
* Upload the new APK to the Google Play Developer Console
* Add what changed to the `Recent Changes` section on the Developer Console
* Copy the signed APK to the `Releases` folder in Dropbox
* Tag the release in git
  # Use the same tag name as the versionName
  git tag v1.1.1
  git push --tags
