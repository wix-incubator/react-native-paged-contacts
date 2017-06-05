import React, {Component} from 'react';
import {PagedContacts} from 'react-native-paged-contacts';
import ReactNative, {Image, View, ListView, Text} from 'react-native';
import _ from 'lodash';

export default class DemoApp extends Component {
  constructor() {
    super();
    this.contacts = [];
    const ds = new ListView.DataSource({rowHasChanged: (r1, r2) => r1 !== r2});
    this.state = {
      dataSource: ds.cloneWithRows([])
    };
    this.handlesByContactId = {};
    this.pagedContacts = new PagedContacts();

    this.getContacts().then(contacts => {
      this.contacts = contacts;
      this.setState({
        dataSource: ds.cloneWithRows(contacts)
      });
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
      <ListView
        style={{marginTop: 20}}
        enableEmptySections={true}
        dataSource={this.state.dataSource}
        renderRow={contact => (
          <View style={{flexDirection: "row", alignItems: 'center'}}>
            <PagedContacts.Image
              style={{width:15, height: 15}}
              pagedContacts={this.pagedContacts}
              contactId={contact.identifier}
            />
            <Text>{contact.displayName}</Text>
          </View>
        )}
      />
    );
  }
}
