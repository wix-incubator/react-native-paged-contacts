import 'react-native';
import React from 'react';

import Example from '../index.ios.js';

// Note: test renderer must be required after react-native.
import renderer from 'react-test-renderer';
import {View} from "react-native";

it('renders correctly', () => {
  const tree = renderer.create(
    <View />
  );
});
