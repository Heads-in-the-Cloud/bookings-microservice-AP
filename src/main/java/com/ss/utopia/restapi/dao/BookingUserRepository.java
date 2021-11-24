package com.ss.utopia.restapi.dao;

import java.util.List;

import com.ss.utopia.restapi.models.*;

import org.springframework.data.repository.CrudRepository;

public interface BookingUserRepository extends CrudRepository<BookingUser, BookingUserPK> {
    public List<BookingUser> findAllByBookingId(int booking);
    public List<BookingUser> findAllByUserId(int user);
}