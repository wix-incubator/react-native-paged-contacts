import React, {Component} from 'react';
import {PagedContacts} from 'react-native-paged-contacts';
import {View, Text} from 'react-native';

export default class DemoApp extends Component {
  constructor() {
    super();
    this.state = {contacts: []};
    this.pagedContacts = new PagedContacts();
    this.getContacts().then(contacts => {
      this.setState({contacts});
    });
  }

  async getContacts() {
    const granted = await this.pagedContacts.requestAccess();
    if(granted) {
      const count = await this.pagedContacts.getContactsCount();
      return await this.pagedContacts.getContactsWithRange(0, count, [PagedContacts.displayName, PagedContacts.phoneNumbers, PagedContacts.emailAddresses]);
    } else {
      console.warn('Permissions issue');
    }
  }

  render() {
    const {contacts} = this.state;

    return (
      <View>
        {contacts.map((contact, index) => (
          <View key={index} style={{flexDirection: "row", alignItems: 'center'}}>
            <PagedContacts.Image source={[]} style={{width:15, height: 15}} pagedContacts={this.pagedContacts} contactId={contact.identifier}/>
            <Text>{contact.displayName}</Text>
          </View>
        ))}
      </View>
    );
  }
}
