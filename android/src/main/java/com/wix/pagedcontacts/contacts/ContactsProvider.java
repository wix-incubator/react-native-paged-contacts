package com.wix.pagedcontacts.contacts;

import android.content.Context;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentResolver;

import android.database.Cursor;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds;
import android.provider.ContactsContract.CommonDataKinds.Organization;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.CommonDataKinds.Note;
import android.provider.ContactsContract.CommonDataKinds.Website;
import android.provider.ContactsContract.RawContacts;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableArray;

import com.wix.pagedcontacts.contacts.Items.Contact;
import com.wix.pagedcontacts.contacts.query.QueryParams;
import com.wix.pagedcontacts.contacts.readers.ContactCursorReader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ContactsProvider {
    public static final int MAX_ARGS = 990;
    private Context context;
    private List<String> contactIds;
    private String matchName;

    public void setMatchName(String matchName) {
        this.matchName = matchName;
        contactIds.clear();
    }

    ContactsProvider(Context context) {
        this.context = context;
        contactIds = new ArrayList<>();
    }

    public int getContactsCount() {
        ensureContactIds();
        return contactIds.size();
    }

    private List<String> getAllContacts() {
        List<String> contactIds = new ArrayList<>();
        Set<String> dedupSet = new HashSet<>();
        Cursor cursor = queryContacts(new QueryParams(matchName));
        ContactCursorReader reader = new ContactCursorReader(context);
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Contact contact = reader.read(cursor);
                if (dedupSet.add(contact.getContactId())) {
                    contactIds.add(contact.getContactId());
                }
            }
            cursor.close();
        }
        return contactIds;
    }

    public WritableArray getContacts(QueryParams params) {
        if (matchName != null) {
            return getContactsWithNameFilter(params);
        } else {
            List<Contact> contactsWithRange = getContactsWithRange(params);
            return toWritableArray(params, new List[]{contactsWithRange});
        }
    }

    private WritableArray getContactsWithNameFilter(QueryParams params) {
        ensureContactIds();
        int size = contactIds.size();
        int offset = 0;
        List<List<Contact>> contactLists = new ArrayList<>();
        while (size > 0) {
            params.size = size > MAX_ARGS ? MAX_ARGS : size;
            params.offset = offset;
            contactLists.add(getContactsWithRange(params));

            offset += params.size;
            size = size - MAX_ARGS;
        }
        List[] lists = contactLists.toArray(new List[contactLists.size()]);
        return toWritableArray(params, lists);
    }

    private List<Contact> getContactsWithRange(QueryParams params) {
        ensureContactIds();
        List<String> contactsToFetch = getContactsToFetch(params);
        if (contactsToFetch.isEmpty()) {
            return Collections.emptyList();
        }
        params.setContactsToFetch(contactsToFetch);
        Cursor cursor = queryContacts(params);
        return new ContactCursorReader(context).readWithIds(cursor);
    }

    public WritableArray getContactsWithIdentifiers(QueryParams params) {
        ensureContactIds();
        params.setContactsToFetch(getContactsToFetch(params));
        Cursor cursor = queryContacts(params);
        List<Contact> contacts = new ContactCursorReader(context).readWithIds(cursor);
        return toWritableArray(params, new List[]{contacts});
    }

    private WritableArray toWritableArray(QueryParams params, List<Contact>[] contactsLists) {
        WritableArray result = Arguments.createArray();
        for (List<Contact> contactList : contactsLists) {
            for (Contact contact : contactList) {
                result.pushMap(contact.toMap(params));
            }
        }
        return result;
    }

    private void ensureContactIds() {
        if (contactIds.isEmpty()) {
            sync();
        }
    }

    private void sync() {
        contactIds = getAllContacts();
    }

    private Cursor queryContacts(QueryParams params) {
        return context.getContentResolver().query(ContactsContract.Data.CONTENT_URI,
                params.getProjection(),
                params.getSelection(),
                params.getSelectionArgs(),
                ContactsContract.Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC"
        );
    }

    private List<String> getContactsToFetch(QueryParams params) {
        if (!params.getIdentifiers().isEmpty()) {
            return params.getIdentifiers();
        }
        List<String> ids = new ArrayList<>();
        int offset = params.offset <= getContactsCount() ? params.offset : getContactsCount() - 1;
        int size = offset + params.size < getContactsCount() ? params.size : getContactsCount() - offset;
        for (int i = offset; i < offset + size; i++) {
            ids.add(contactIds.get(i));
        }
        return ids;
    }

    public void saveContact(ReadableMap contact, Promise promise) {

        String givenName = contact.hasKey("givenName") ? contact.getString("givenName") : null;
        String middleName = contact.hasKey("middleName") ? contact.getString("middleName") : null;
        String familyName = contact.hasKey("familyName") ? contact.getString("familyName") : null;
        String prefix = contact.hasKey("prefix") ? contact.getString("prefix") : null;
        String suffix = contact.hasKey("suffix") ? contact.getString("suffix") : null;
        String company = contact.hasKey("company") ? contact.getString("company") : null;
        String jobTitle = contact.hasKey("jobTitle") ? contact.getString("jobTitle") : null;
        String department = contact.hasKey("department") ? contact.getString("department") : null;
        String note = contact.hasKey("note") ? contact.getString("note") : null;
        String thumbnailPath = contact.hasKey("thumbnailPath") ? contact.getString("thumbnailPath") : null;

        ReadableArray phoneNumbers = contact.hasKey("phoneNumbers") ? contact.getArray("phoneNumbers") : null;


        int numOfPhones = 0;
        String[] phones = null;
        Integer[] phonesTypes = null;
        String[] phonesLabels = null;
        if (phoneNumbers != null) {
            numOfPhones = phoneNumbers.size();
            phones = new String[numOfPhones];
            phonesTypes = new Integer[numOfPhones];
            phonesLabels = new String[numOfPhones];
            for (int i = 0; i < numOfPhones; i++) {
                phones[i] = phoneNumbers.getMap(i).getString("number");
                String label = phoneNumbers.getMap(i).getString("label");
                phonesTypes[i] = mapStringToPhoneType(label);
                phonesLabels[i] = label;
            }
        }

        ReadableArray urlAddresses = contact.hasKey("urlAddresses") ? contact.getArray("urlAddresses") : null;
        int numOfUrls = 0;
        String[] urls = null;
        if (urlAddresses != null) {
            numOfUrls = urlAddresses.size();
            urls = new String[numOfUrls];
            for (int i = 0; i < numOfUrls; i++) {
                urls[i] = urlAddresses.getMap(i).getString("url");
            }
        }

        ReadableArray emailAddresses = contact.hasKey("emailAddresses") ? contact.getArray("emailAddresses") : null;
        int numOfEmails = 0;
        String[] emails = null;
        Integer[] emailsTypes = null;
        String[] emailsLabels = null;
        if (emailAddresses != null) {
            numOfEmails = emailAddresses.size();
            emails = new String[numOfEmails];
            emailsTypes = new Integer[numOfEmails];
            emailsLabels = new String[numOfEmails];
            for (int i = 0; i < numOfEmails; i++) {
                emails[i] = emailAddresses.getMap(i).getString("email");
                String label = emailAddresses.getMap(i).getString("label");
                emailsTypes[i] = mapStringToEmailType(label);
                emailsLabels[i] = label;
            }
        }

        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();

        ContentProviderOperation.Builder op = ContentProviderOperation.newInsert(RawContacts.CONTENT_URI)
                .withValue(RawContacts.ACCOUNT_TYPE, null)
                .withValue(RawContacts.ACCOUNT_NAME, null);
        ops.add(op.build());

        op = ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE)
                .withValue(StructuredName.GIVEN_NAME, givenName)
                .withValue(StructuredName.MIDDLE_NAME, middleName)
                .withValue(StructuredName.FAMILY_NAME, familyName)
                .withValue(StructuredName.PREFIX, prefix)
                .withValue(StructuredName.SUFFIX, suffix);
        ops.add(op.build());

        op = ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE, Note.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Note.NOTE, note);
        ops.add(op.build());

        op = ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE, Organization.CONTENT_ITEM_TYPE)
                .withValue(Organization.COMPANY, company)
                .withValue(Organization.TITLE, jobTitle)
                .withValue(Organization.DEPARTMENT, department);
        ops.add(op.build());

        op.withYieldAllowed(true);

        for (int i = 0; i < numOfPhones; i++) {
            op = ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE, CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    .withValue(CommonDataKinds.Phone.NUMBER, phones[i])
                    .withValue(CommonDataKinds.Phone.TYPE, phonesTypes[i])
                    .withValue(CommonDataKinds.Phone.LABEL, phonesLabels[i]);
            ops.add(op.build());
        }

        for (int i = 0; i < numOfUrls; i++) {
            op = ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE, CommonDataKinds.Website.CONTENT_ITEM_TYPE)
                    .withValue(CommonDataKinds.Website.URL, urls[i]);
            ops.add(op.build());
        }

        for (int i = 0; i < numOfEmails; i++) {
            op = ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE, CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                    .withValue(CommonDataKinds.Email.ADDRESS, emails[i])
                    .withValue(CommonDataKinds.Email.TYPE, emailsTypes[i])
                    .withValue(CommonDataKinds.Email.LABEL, emailsLabels[i]);
            ops.add(op.build());
        }

        ReadableArray postalAddresses = contact.hasKey("postalAddresses") ? contact.getArray("postalAddresses") : null;
        if (postalAddresses != null) {
            for (int i = 0; i < postalAddresses.size(); i++) {
                ReadableMap address = postalAddresses.getMap(i);

                op = ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                        .withValue(ContactsContract.Data.MIMETYPE, CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE)
                        .withValue(CommonDataKinds.StructuredPostal.TYPE, mapStringToPostalAddressType(address.getString("label")))
                        .withValue(CommonDataKinds.StructuredPostal.LABEL, address.getString("label"))
                        .withValue(CommonDataKinds.StructuredPostal.STREET, address.getString("street"))
                        .withValue(CommonDataKinds.StructuredPostal.CITY, address.getString("city"))
                        .withValue(CommonDataKinds.StructuredPostal.REGION, address.getString("state"))
                        .withValue(CommonDataKinds.StructuredPostal.POSTCODE, address.getString("postCode"))
                        .withValue(CommonDataKinds.StructuredPostal.COUNTRY, address.getString("country"));

                ops.add(op.build());
            }
        }

            try {
            ContentResolver cr = this.context.getContentResolver();
            ContentProviderResult[] result = cr.applyBatch(ContactsContract.AUTHORITY, ops);

            promise.resolve(null);

            } catch (Exception e) {
                promise.reject(e);
            }
//        }
    }

    private int mapStringToPhoneType(String label) {
        int phoneType;
        switch (label) {
            case "home":
                phoneType = CommonDataKinds.Phone.TYPE_HOME;
                break;
            case "work":
                phoneType = CommonDataKinds.Phone.TYPE_WORK;
                break;
            case "mobile":
                phoneType = CommonDataKinds.Phone.TYPE_MOBILE;
                break;
            case "main":
                phoneType = CommonDataKinds.Phone.TYPE_MAIN;
                break;
            case "work fax":
                phoneType = CommonDataKinds.Phone.TYPE_FAX_WORK;
                break;
            case "home fax":
                phoneType = CommonDataKinds.Phone.TYPE_FAX_HOME;
                break;
            case "pager":
                phoneType = CommonDataKinds.Phone.TYPE_PAGER;
                break;
            case "work_pager":
                phoneType = CommonDataKinds.Phone.TYPE_WORK_PAGER;
                break;
            case "work_mobile":
                phoneType = CommonDataKinds.Phone.TYPE_WORK_MOBILE;
                break;
            default:
                phoneType = CommonDataKinds.Phone.TYPE_CUSTOM;
                break;
        }
        return phoneType;
    }

    private int mapStringToEmailType(String label) {
        int emailType;
        switch (label) {
            case "home":
                emailType = CommonDataKinds.Email.TYPE_HOME;
                break;
            case "work":
                emailType = CommonDataKinds.Email.TYPE_WORK;
                break;
            case "mobile":
                emailType = CommonDataKinds.Email.TYPE_MOBILE;
                break;
            default:
                emailType = CommonDataKinds.Email.TYPE_CUSTOM;
                break;
        }
        return emailType;
    }

    private int mapStringToPostalAddressType(String label) {
        int postalAddressType;
        switch (label) {
            case "home":
                postalAddressType = CommonDataKinds.StructuredPostal.TYPE_HOME;
                break;
            case "work":
                postalAddressType = CommonDataKinds.StructuredPostal.TYPE_WORK;
                break;
            default:
                postalAddressType = CommonDataKinds.StructuredPostal.TYPE_CUSTOM;
                break;
        }
        return postalAddressType;
    }

}
