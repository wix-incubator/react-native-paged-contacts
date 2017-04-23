import React, {Component} from 'react';
import {PagedContacts} from 'react-native-paged-contacts';
import {View, Text} from 'react-native';

export default class DemoApp extends Component {
  constructor() {
    super();
    this.state = {contacts: []};
    this.getContacts().then(contacts => {
      this.setState({contacts});
    });
  }

  async getContacts() {
    const pagedContacts = new PagedContacts();
    const granted = await pagedContacts.requestAccess();
    if(granted) {
      const count = await pagedContacts.getContactsCount();
      return await pagedContacts.getContactsWithRange(0, count, [PagedContacts.displayName, PagedContacts.thumbnailImageData, PagedContacts.phoneNumbers, PagedContacts.emailAddresses])
    }
  }

  render() {
    const {contacts} = this.state;

    return (
      <View>
        {contacts.map((contact, index) => <Text key={index}>{contact.displayName}</Text>)}
      </View>
    );
  }
}
