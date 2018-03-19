import React, {Component} from 'react';
import {PagedContacts} from 'react-native-paged-contacts';
import {SafeAreaView, FlatList} from 'react-native';
import {View, Text, LoaderScreen, Colors} from 'react-native-ui-lib';//eslint-disable-line


export default class DemoApp extends Component {
  constructor() {
    super();
    this.state = {
      data: undefined,
      loading: true,
    };

    this.pagedContacts = new PagedContacts();

    this.getContacts().then(contacts => {
      this.contacts = contacts;

      let list = contacts.map(x => {
        return {
          key: x.identifier,
          label: x.displayName
        }
      });
      this.setState({
        data: list,
        loading: false
      });
    });
  }

  componentDidMount() {
    setTimeout(() => {
      this.setState({
        animationConfig: {
          animation: 'fadeOut',
          onAnimationEnd: () => this.setState({loading: false}),
        },
      });
    }, 2500);
  }

  async getContacts() {
    const granted = await this.pagedContacts.requestAccess();
    if (granted) {
      const count = await this.pagedContacts.getContactsCount();
      this.setState({
        count: count
      });
      return await this.pagedContacts.getContactsWithRange(0, count, [PagedContacts.identifier, PagedContacts.displayName, PagedContacts.phoneNumbers, PagedContacts.emailAddresses]);

    } else {
      console.warn('Permissions issue');
    }
  }

  render() {
    const {data} = this.state;
    if (data === undefined) {
      return this.renderLoading();
    }
    return this.renderList();
  }

  renderList() {
    const {data, count} = this.state;
    return (
      <SafeAreaView>
        <Text style={{fontWeight: 'bold'}}>Found {count} contacts</Text>
        <FlatList
          data={data}
          renderItem={({item}) => <Text>{item.label}</Text>}
        />
      </SafeAreaView>
    );
  }

  renderLoading() {
    const {loading, animationConfig} = this.state;
    return (
      <View flex bg-orange70 center>
        {loading &&
        <LoaderScreen
          color={Colors.blue60}
          message="Loading..."
          overlay
          {...animationConfig}
        />}
      </View>
    );
  }
}
