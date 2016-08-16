/**
 * @flow
 */

import React, { Component } from 'react'
import {
  Text,
  View,
  Dimensions,
  ScrollView,
  StyleSheet,
  AppRegistry,
  TouchableNativeFeedback,
} from 'react-native'

const { width, height } = Dimensions.get('window')

import {
  CoordinatorLayout,
  BottomSheetBehavior,
} from './lib'

class bottomSheetBehavior extends Component {
  state = {
    buttons: [0]
  };

  handleAddButton() {
    const { buttons } = this.state
    const length = buttons.length || 0
    this.setState({ buttons: [
      ...buttons,
      length + 1
    ]})
  }

  handleRemoveButton(index) {
    const { buttons } = this.state
    this.setState({ buttons: [
      ...buttons.slice(0, index),
      ...buttons.slice(index + 1)
    ]})
  }

  renderButton(key, index) {
    return (
      <TouchableNativeFeedback key={index} onPress={() => this.handleRemoveButton(index)}>
        <View style={styles.button}>
          <Text style={styles.buttonLabel}>{key}</Text>
        </View>
      </TouchableNativeFeedback>
    )
  }

  render() {
    return (
      <CoordinatorLayout style={styles.container}>
        <View style={styles.content}>
          <Text style={styles.welcome}>
            Welcome to React Native!
          </Text>
          <Text style={styles.instructions}>
            To get started, edit index.ios.js
          </Text>
          <Text style={styles.instructions}>
            Press Cmd+R to reload,{'\n'}
            Cmd+D or shake for dev menu
          </Text>
          <TouchableNativeFeedback onPress={this.handleAddButton.bind(this)}>
            <View style={styles.button}>
              <Text style={styles.buttonLabel}>Add Item</Text>
            </View>
          </TouchableNativeFeedback>
        </View>
        <BottomSheetBehavior>
          <View style={styles.bottomSheet}>
            <View style={styles.bottomSheetHeader}>
              <Text style={styles.label}>BottomSheetBehavior !</Text>
            </View>
            <View style={styles.bottomSheetContent}>
              {this.state.buttons.map(this.renderButton.bind(this))}
            </View>
          </View>
        </BottomSheetBehavior>
      </CoordinatorLayout>
    )
  }
}


const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#F5FCFF',
  },
  content: {
    paddingTop: 24,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#F5FCFF',
  },
  welcome: {
    fontSize: 20,
    textAlign: 'center',
    margin: 10,
  },
  instructions: {
    textAlign: 'center',
    color: '#333333',
    marginBottom: 5,
  },
  bottomSheet: {
    backgroundColor: '#3F51B5',
  },
  bottomSheetHeader: {
    padding: 12,
  },
  bottomSheetContent: {
    padding: 18,
    alignItems: 'center',
    backgroundColor: 'white',
  },
  button: {
    padding: 6,
    paddingHorizontal: 14,
    height: 30,
    alignSelf: 'stretch',
    alignItems: 'center',
    marginVertical: 1,
    backgroundColor: '#333',
  },
  buttonLabel: {
    color: 'white'
  },
  label: {
    fontSize: 18,
    fontWeight: 'bold',
    color: 'white',
  },
})

AppRegistry.registerComponent('bottomSheetBehavior', () => bottomSheetBehavior)
