import React, {
  AppRegistry,
  Component,
  StyleSheet,
  Text,
  View,
  NativeModules
} from 'react-native';

// import ReactNativeUA from 'react-native-ua';

class UrbanAirshipNotification extends Component {

  render() {

    // ReactNativeUA.enable_notification()
    // ReactNativeUA.disable_notification()
    // ReactNativeUA.set_tags(["test-a", "test-b", "test-c"])
    // ReactNativeUA.add_tag("test-react-native-2")
    // ReactNativeUA.remove_tag("test-a")
    // ReactNativeUA.unsubscribe_to("receivedNotification")
    // ReactNativeUA.subscribe_to("receivedNotification", (notification) => {
    //   alert(notification.type + ": " + notification.data.aps.alert + " - " + notification.data.link)
    // })

    return (
      <View style={styles.container}>
        <Text style={styles.title}>iOS</Text>
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#F5FCFF',
  },
  title: {
    fontSize: 40,
    textAlign: 'center'
  }
});

AppRegistry.registerComponent('SampleApp', () => UrbanAirshipNotification);
