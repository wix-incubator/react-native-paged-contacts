import React, {Component} from 'react';
import {PagedContacts} from 'react-native-paged-contacts';
import {SafeAreaView, FlatList, Text} from 'react-native';

export default class DemoApp extends Component {
  constructor() {
    super();
    this.state = {
      data: {}
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
        data: list
      });
    });
  }

  async getContacts() {
    const granted = await this.pagedContacts.requestAccess();
    if (granted) {
      const count = await this.pagedContacts.getContactsCount();
      return await this.pagedContacts.getContactsWithRange(0, 100, [PagedContacts.identifier, PagedContacts.displayName, PagedContacts.phoneNumbers, PagedContacts.emailAddresses]);
    } else {
      console.warn('Permissions issue');
    }
  }

  render() {
    const {data} = this.state;
    return (
      <SafeAreaView>
        <Text style={{fontWeight: 'bold'}}>Found {data.length} contacts</Text>
        <FlatList
          data={data}
          renderItem={({item}) => <Text>{item.label}</Text>}
        />
      </SafeAreaView>
    );
  }
}
