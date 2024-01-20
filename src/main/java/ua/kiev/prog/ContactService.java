package ua.kiev.prog;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.kiev.prog.jsonUploaders.JsonContactManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ContactService {
    private final ContactRepository contactRepository;
    private final GroupRepository groupRepository;
    List<Contact> contactsToSave = new ArrayList<>();
    List<Group> groups = new ArrayList<>();

    public ContactService(ContactRepository contactRepository,
                          GroupRepository groupRepository) {
        this.contactRepository = contactRepository;
        this.groupRepository = groupRepository;
    }

    public void saveContacts() {
        JsonContactManager contactJsonFileManager = new JsonContactManager("");
        List<Contact> contacts = contactRepository.findAll();
        contactJsonFileManager.saveJsonForFile(contacts);
    }

    @Transactional
    public void uploadContactsFromJson(){
        JsonContactManager contactJsonFileManager = new JsonContactManager("");
        groups = groupRepository.findAll();
        List<Contact> jsonContacts = contactJsonFileManager.uploadJsonForFile();
        for (Contact contact : jsonContacts) {
            if (!isContactPresentAndEqual(contact)) {
                if (contact.getGroup() != null) {
                    addContactToGroup(contact);
                }
                contactsToSave.add(contact);
            }
        }
        contactRepository.saveAll(contactsToSave);
        groupRepository.saveAll(groups);
    }
    private boolean isContactPresentAndEqual(Contact contact){
        boolean result = false;
        Optional<Contact> optionalContact = contactRepository.findById(contact.getId());
        if (optionalContact.isPresent()) {
            Contact contactFromBd = optionalContact.get();
            if (contact.equals(contactFromBd)) {
                result = true;
            } else {
                throw new RuntimeException("The database has a contact with ID " + contact.getId() + " but with different field values");
            }
        }
        return result;
    }

    private void addContactToGroup(Contact contact){
        Group group = findGroupByName(contact.getGroup().getName());
        if(group != null){
            groups.indexOf(group);
            contact.setGroup(group);
        }
    }

    private Group findGroupByName(String groupName) {
        Optional<Group> optionalGroup = groups.stream()
                .filter(group -> group.getName().equals(groupName))
                .findFirst();
        return optionalGroup.orElse(null);
    }

    @Transactional
    public void addContact(Contact contact) {
        contactRepository.save(contact);
    }

    @Transactional
    public void addGroup(Group group) {
        groupRepository.save(group);
    }

    @Transactional
    public void deleteGroup(Long groupId){
        Group group = groupRepository.findById(groupId).get();
        List<Contact> contacts = contactRepository.findByGroup(group);
        for (Contact contact : contacts) {
            contact.setGroup(null);
        }
        group.setContacts(null);
        contactRepository.deleteAll(contacts);
        groupRepository.delete(group);
    }

    @Transactional
    public void deleteContacts(long[] idList) {
        for (long id : idList)
            contactRepository.deleteById(id);
    }

    @Transactional(readOnly=true)
    public List<Group> findGroups() {
        return groupRepository.findAll();
    }

    @Transactional(readOnly=true)
    public List<Contact> findAll(Pageable pageable) {
        return contactRepository.findAll(pageable).getContent();
    }

    @Transactional(readOnly=true)
    public List<Contact> findByGroup(Group group, Pageable pageable) {
        return contactRepository.findByGroup(group);
    }

    @Transactional(readOnly = true)
    public long countByGroup(Group group) {
        return contactRepository.countByGroup(group);
    }

    @Transactional(readOnly=true)
    public List<Contact> findByPattern(String pattern, Pageable pageable) {
        return contactRepository.findByPattern(pattern, pageable);
    }

    @Transactional(readOnly = true)
    public long count() {
        return contactRepository.count();
    }

    @Transactional(readOnly=true)
    public Group findGroup(long id) {
        return groupRepository.findById(id).get();
    }

    @Transactional
    public void reset() {
        groupRepository.deleteAll();
        contactRepository.deleteAll();

        Group group = new Group("Test");
        Contact contact;

        addGroup(group);

        for (int i = 0; i < 13; i++) {
            contact = new Contact(null, "Name" + i, "Surname" + i, "1234567" + i, "user" + i + "@test.com");
            addContact(contact);
        }
        for (int i = 0; i < 10; i++) {
            contact = new Contact(group, "Other" + i, "OtherSurname" + i, "7654321" + i, "user" + i + "@other.com");
            addContact(contact);
        }
    }
}
