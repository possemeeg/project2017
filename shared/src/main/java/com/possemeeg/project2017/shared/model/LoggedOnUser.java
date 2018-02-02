package com.possemeeg.project2017.shared.model;

public class LoggedOnUser implements DataSerializable {
    private String username;
    private ProcessContext processContext;
    public LoggedOnUser() {
    }
    public LoggedOnUser(String username, ProcessContext processIndex) {
        this.username = username;
        this.processContext = processContext;
    }
    public getUsername() {
        return username;
    }
    public getProcessContext() {
        return processContext;
    }

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
        out.writeUTF(username);
        processIndex.writeData(out);
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        username = in.readUTF();
        processContext = ProcessContext.fromObjectDataInput(in);
    }
}


