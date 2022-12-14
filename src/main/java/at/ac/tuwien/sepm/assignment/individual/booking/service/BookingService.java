package at.ac.tuwien.sepm.assignment.individual.booking.service;

import at.ac.tuwien.sepm.assignment.individual.booking.entities.Booking;
import at.ac.tuwien.sepm.assignment.individual.booking.exception.ServiceException;

import java.util.List;

public interface BookingService {

    /**create new Booking
     * @param booking
     * @return booking with
     * @throws ServiceException
     */
    Booking create(Booking booking) throws ServiceException;

    /**delete booking
     * @param booking
     * @return booking
     * @throws ServiceException
     */
    Booking delete(Booking booking) throws ServiceException;

    /**cancel Booking
     * @param booking
     * @return booking
     * @throws ServiceException
     */
    Booking cancel(Booking booking) throws ServiceException;

    /**completed Booking
     * @param booking
     * @return booking
     * @throws ServiceException
     */
    Booking completed (Booking booking) throws ServiceException;

    /**return list of booking
     * @return
     * @throws ServiceException
     */
    List<Booking> getAllBooking() throws ServiceException;

    /**
     * get list of booking by id
     * @param id
     * @return
     * @throws ServiceException
     */
    Booking getBookingByBid(int id) throws ServiceException;
}
