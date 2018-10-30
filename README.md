
# react-native-sendsms

## Getting started

`$ npm install react-native-sendsms --save`

### Mostly automatic installation

`$ react-native link react-native-sendsms`

### Manual installation


#### Android

1. Open up `android/app/src/main/java/[...]/MainActivity.java`
  - Add `import com.robixxu.RNSendsmsPackage;` to the imports at the top of the file
  - Add `new RNSendsmsPackage()` to the list returned by the `getPackages()` method
2. Append the following lines to `android/settings.gradle`:
  	```
  	include ':react-native-sendsms'
  	project(':react-native-sendsms').projectDir = new File(rootProject.projectDir, 	'../node_modules/react-native-sendsms/android')
  	```
3. Insert the following lines inside the dependencies block in `android/app/build.gradle`:
  	```
      compile project(':react-native-sendsms')
  	```


## Usage
```javascript
import RNSendsms from 'react-native-sendsms';

// TODO: What to do with the module?
RNSendsms;
```
  