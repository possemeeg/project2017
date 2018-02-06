package com.possemeeg.project2017.shared.model;

public class LoggedOnUser implements DataSerializable {
    private String username;
    private String memberUuid;
    public LoggedOnUser() {
    }
    public LoggedOnUser(String username, String memberUuid) {
        this.username = username;
        this.memberUuid = memberUuid;
    }
    public getUsername() {
        return username;
    }
    public getMemberUuid() {
        return memberUuid;
    }

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
        out.writeUTF(username);
        out.writeUTF(memberUuid);
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        username = in.readUTF();
        memberUuid = in.readUTF();
    }
}


