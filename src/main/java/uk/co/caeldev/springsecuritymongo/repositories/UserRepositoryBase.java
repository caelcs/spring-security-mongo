package uk.co.caeldev.springsecuritymongo.repositories;

public interface UserRepositoryBase {

    boolean changePassword(String oldPassword, String newPassword, String username);

}
