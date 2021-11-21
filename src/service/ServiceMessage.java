package service;

import domain.Message;
import domain.User;
import repository.Repository;

public class ServiceMessage {

    private Repository<Long, Message> repoMessage;
    private Repository<Long, User> repoUser;

    public ServiceMessage(Repository<Long, Message> repoMessage, Repository<Long, User> repoUser) {
        this.repoMessage = repoMessage;
        this.repoUser = repoUser;
    }


}
