# React Native Paged Contacts

Paged contacts manager for React Native.

**Currently, only fetching contacts is supported.**

##API

- `new PagedContacts()` — Create a paged contacts manager for all device contacts.
- `new PagedContacts(nameMatch)` — Create a paged contacts manager for contacts matching the provided name.
- `requestAccess()` — Request contacts access from the operating system. This must be called before calling other APIs.
- `setNameMatch(matchName)` — Change the result set to filter contacts by matching name. Set to `null` to receive all contacts.
- `getContactsCount()` — Get the count of the current contacts set.
- `getContactsWithRange(offset, batchSize, keysToFetch)` — Get contacts within the requested `batchSize`, starting from `offset`. Only the keys requested in `keysToFetch` will be provided (contact identifiers are always provided).
- `getContactsWithIdentifiers(identifiers, keysToFetch)` — Get contacts with the provided `identifiers`. Only the keys requested in `keysToFetch` will be provided (contact identifiers are always provided).
- `dispose()` — Disposes the native components. Call this method when the manager object is no longer required. Must not call any other methods of the contacts manager after calling `dispose`.