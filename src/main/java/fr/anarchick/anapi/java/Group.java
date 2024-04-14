package fr.anarchick.anapi.java;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class Group<T> {

    private List<T> members = new ArrayList<>();

    public boolean has(T member) {
        return members.contains(member);
    }

    public void addMembers(T... members) {
        for (T member : members) {
            if (!this.members.contains(member)) {
                this.members.add(member);
            }
        }
    }

    public void setMembers(T... members) {
        this.members = new ArrayList<>(List.of(members));
    }

    public void removeMembers(T... members) {
        for (T member : members) {
            this.members.remove(member);
        }
    }

    public void removeAllMembers() {
        members.clear();
    }

    public List<T> getMembers() {
        return members;
    }

    public Group fusion(Group grouo) {
        Group newGroup = new Group();
        newGroup.addMembers(newGroup.getMembers());
        newGroup.addMembers(getMembers());
        return newGroup;
    }

    public int size() {
        return this.members.size();
    }

}
