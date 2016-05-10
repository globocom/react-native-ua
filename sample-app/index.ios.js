import React, {
  AppRegistry,
  Component,
  StyleSheet,
  Text,
  View,
  NativeModules
} from 'react-native';

import Button from 'react-native-button';
import Prompt from 'react-native-prompt';

import ReactNativeUA from 'react-native-ua';

class UrbanAirshipNotification extends Component {

  constructor(props) {
    super(props);

    this.state = {
      show_add_tag_prompt: false,
      show_remove_tag_prompt: false
    };
  }

  render () {

    ReactNativeUA.subscribe_to("receivedNotification", (notification) => {
      alert(notification.type + ": " + notification.data.aps.alert + " - " + notification.data.link)
    })

    return (
      <View style={styles.container}>
        <Button
          style={styles.button}
          onPress={this._handle_enable_notification}>Enable Notification</Button>

        <Button
          style={styles.button}
          onPress={this._handle_disable_notification}>Disable Notification</Button>

        <Button
          style={styles.button}
          onPress={() => this.setState({ show_add_tag_prompt: true })}>Add Tag</Button>

        <Prompt
          title="Add Tag"
          placeholder="tag-example"
          visible={ this.state.show_add_tag_prompt }
          onCancel={ () => { this.setState({ show_add_tag_prompt: false }); }}
          onSubmit={ (tag) => {
            if (tag.length > 0) ReactNativeUA.add_tag(tag);
            this.setState({ show_add_tag_prompt: false });
            alert('Tag added!'); }}/>

        <Button
          style={styles.button}
          onPress={() => this.setState({ show_remove_tag_prompt: true })}>Remove Tag</Button>

        <Prompt
          title="Remove Tag"
          placeholder="tag-example"
          visible={ this.state.show_remove_tag_prompt }
          onCancel={ () => { this.setState({ show_remove_tag_prompt: false }); }}
          onSubmit={ (tag) => {
            if (tag.length > 0) ReactNativeUA.remove_tag(tag);
            this.setState({ show_remove_tag_prompt: false });
            alert('Tag removed!'); }}/>
      </View>
    );
  }

  _handle_enable_notification (event) {
    ReactNativeUA.enable_notification();
    alert('Notification enabled!');
  }

  _handle_disable_notification (event) {
    ReactNativeUA.disable_notification();
    alert('Notification disabled!');
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#F5FCFF',
  },

  button: {
    borderColor: 'black',
    padding: 10,
    margin: 5,
    borderRadius: 5,
    borderWidth: 1,
    fontSize: 27,
    color: 'black'
  }
});

AppRegistry.registerComponent('SampleApp', () => UrbanAirshipNotification);
