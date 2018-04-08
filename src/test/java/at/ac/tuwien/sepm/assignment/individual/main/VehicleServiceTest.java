package at.ac.tuwien.sepm.assignment.individual.main;

import at.ac.tuwien.sepm.assignment.individual.booking.entities.SearchFilter;
import at.ac.tuwien.sepm.assignment.individual.booking.entities.Vehicle;
import at.ac.tuwien.sepm.assignment.individual.booking.exception.ServiceException;
import at.ac.tuwien.sepm.assignment.individual.booking.service.VehicleService;
import at.ac.tuwien.sepm.assignment.individual.booking.service.VehicleServiceImp;
import at.ac.tuwien.sepm.assignment.individual.booking.util.DBUtil;
import org.junit.Test;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class VehicleServiceTest {

    private final VehicleService vehicleService = new VehicleServiceImp(DBUtil.getConnection());

    public VehicleServiceTest() throws SQLException {
    }

    @Test
    public void testVechicleSearchUpperLowerCaseTest() throws ServiceException {
        Vehicle vehicle = new Vehicle();
        vehicle.setModel("car4");
        vehicle.setConstructionYear(1999);
        vehicle.setSeating(2);
        vehicle.setPlateNumber("w1234");
        vehicle.setPowerUnit("Brawn");
        vehicle.setPower(11);
        vehicle.setBasePrice(11);
        vehicle.setCreateDate(new Timestamp(System.currentTimeMillis()));
        vehicleService.create(vehicle);

        SearchFilter searchFilter = new SearchFilter();
        searchFilter.setModel("cAr4");

        Vehicle vehicle1 = null;
        List<Vehicle> search = vehicleService.search(searchFilter);
        for (Vehicle vehicle2 : search){
            if (vehicle2.getVid() == vehicle.getVid()){
                vehicle1 = vehicle2;
            }
        }
        assertTrue(vehicle1 != null);
    }

    @Test
    public void testSearchSuccess() throws ServiceException {
        Vehicle vehicle = new Vehicle();
        vehicle.setModel("car4");
        vehicle.setConstructionYear(1999);
        vehicle.setSeating(2);
        vehicle.setPlateNumber("w1234");
        vehicle.setPowerUnit("Brawn");
        vehicle.setPower(11);
        vehicle.setBasePrice(11);
        vehicle.setCreateDate(new Timestamp(System.currentTimeMillis()));
        vehicleService.create(vehicle);

        SearchFilter searchFilter = new SearchFilter();
        searchFilter.setModel("cAr4");
        searchFilter.setPower("Brawn");

        Vehicle vehicle1 = null;
        List<Vehicle> search = vehicleService.search(searchFilter);
        for (Vehicle vehicle2 : search){
            if (vehicle2.getVid() == vehicle.getVid()){
                vehicle1 = vehicle2;
            }
        }
        assertTrue(vehicle1 != null);
    }

    @Test
    public void testSearchFail() throws ServiceException {
        Vehicle vehicle = new Vehicle();
        vehicle.setModel("car4");
        vehicle.setConstructionYear(1999);
        vehicle.setSeating(2);
        vehicle.setPlateNumber("w1234");
        vehicle.setPowerUnit("Brawn");
        vehicle.setPower(11);
        vehicle.setBasePrice(11);
        vehicle.setCreateDate(new Timestamp(System.currentTimeMillis()));
        vehicleService.create(vehicle);

        SearchFilter searchFilter = new SearchFilter();
        searchFilter.setModel("cAr4");
        searchFilter.setPower("Motorized");

        Vehicle vehicle1 = null;
        List<Vehicle> search = vehicleService.search(searchFilter);
        for (Vehicle vehicle2 : search){
            if (vehicle2.getVid() == vehicle.getVid()){
                vehicle1 = vehicle2;
            }
        }
        assertFalse(vehicle1 != null);
    }

    @Test
    public void testModelLength() throws ServiceException {
        Vehicle vehicle = new Vehicle();
        vehicle.setModel("car4asdfdsaf asdfadsfdsafasdfasfdsa");
        vehicle.setConstructionYear(1999);
        vehicle.setSeating(2);
        vehicle.setPlateNumber("w1234");
        vehicle.setPowerUnit("Brawn");
        vehicle.setPower(11);
        vehicle.setBasePrice(11);
        vehicle.setCreateDate(new Timestamp(System.currentTimeMillis()));
        Vehicle vehicle1 = vehicleService.create(vehicle);

        assertFalse(vehicle1 != null);
    }

}
