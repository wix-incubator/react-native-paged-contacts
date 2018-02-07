import React, {Component} from 'react';
import {
  AppRegistry,
  StyleSheet,
  View
} from 'react-native';

import DemoApp from './src/demo-app.component';

class Example extends Component {

  constructor(props) {
    super(props);
    this.state = {
      example: undefined
    };
  }

  render() {
    if (this.state.example) {
      const Example = this.state.example;
      return <Example/>;
    }
    return (
      <View style={styles.container}>
        <DemoApp/>
      </View>
    );
  }

}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    padding: 4,
    backgroundColor: '#F5FCFF',
  }
});

AppRegistry.registerComponent('Example', () => Example);