package com.retailstore.discountservice;

import com.retailstore.discountservice.constant.ItemType;
import com.retailstore.discountservice.constant.UserType;
import com.retailstore.discountservice.model.Discount;
import com.retailstore.discountservice.model.Item;
import com.retailstore.discountservice.model.User;
import com.retailstore.discountservice.repository.DiscountRepository;
import com.retailstore.discountservice.request.BillRequest;
import com.retailstore.discountservice.service.DiscountService;
import com.retailstore.discountservice.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class DiscountServiceTests {

    @Mock
    DiscountRepository discountRepository;

    @Mock
    UserService userService;

    @InjectMocks
    DiscountService discountService;

    @Test
    public void testCalculateForEmployee() {
        List<UserType> userTypes = List.of(UserType.CUSTOMER, UserType.EMPLOYEE);
        User user = new User(1L, "Test User", userTypes, LocalDateTime.now());

        List<Item> items = List.of(
                new Item(1L, "Toaster", "Toaster", 1500.0, ItemType.ELECTRONIC),
                new Item(2L, "Corn Flour", "Corn Flour", 500.0, ItemType.GROCERY),
                new Item(3L, "Make-up Kit", "Make-up Kit", 1000.0, ItemType.COSMETIC));

        Discount percentageBasedDiscountForEmployee =
                new Discount(1L,"Employee Discount", "Percentage based discount for store employees", 30.0, 0.0, 0.0, true, UserType.EMPLOYEE);
        Discount billAmountBasedDiscountForCustomer =
                new Discount(4L,"Bill Amount Based Discount", "Bill amount based discount for store customers", 0.0, 5.0, 100.0, false, UserType.CUSTOMER);

        when(discountRepository.findByDiscountUserTypeAndIsPercentageBasedDiscount(UserType.EMPLOYEE, Boolean.TRUE)).thenReturn(percentageBasedDiscountForEmployee);
        when(discountRepository.findByDiscountUserTypeAndIsPercentageBasedDiscount(UserType.CUSTOMER, Boolean.FALSE)).thenReturn(billAmountBasedDiscountForCustomer);

        Double netPayableAmount = discountService.calculateDiscount(new BillRequest(user, items));

        assertEquals(2100, netPayableAmount);
    }

    @Test
    public void testCalculateForAffiliate() {
        List<UserType> userTypes = List.of(UserType.CUSTOMER, UserType.AFFILIATE);
        User user = new User(1L, "Test User", userTypes, LocalDateTime.now());

        List<Item> items = List.of(
                new Item(1L, "Toaster", "Toaster", 1500.0, ItemType.ELECTRONIC),
                new Item(2L, "Corn Flour", "Corn Flour", 500.0, ItemType.GROCERY),
                new Item(3L, "Make-up Kit", "Make-up Kit", 1000.0, ItemType.COSMETIC));

        Discount percentageBasedDiscountForAffiliate =
                new Discount(2L,"Affiliate Discount", "Percentage based discount for store affiliate", 10.0, 0.0, 0.0, true, UserType.AFFILIATE);
        Discount billAmountBasedDiscountForCustomer =
                new Discount(4L,"Bill Amount Based Discount", "Bill amount based discount for store customers", 0.0, 5.0, 100.0, false, UserType.CUSTOMER);

        when(discountRepository.findByDiscountUserTypeAndIsPercentageBasedDiscount(UserType.AFFILIATE, Boolean.TRUE)).thenReturn(percentageBasedDiscountForAffiliate);
        when(discountRepository.findByDiscountUserTypeAndIsPercentageBasedDiscount(UserType.CUSTOMER, Boolean.FALSE)).thenReturn(billAmountBasedDiscountForCustomer);

        Double netPayableAmount = discountService.calculateDiscount(new BillRequest(user, items));

        assertEquals(2600, netPayableAmount);
    }

    @Test
    public void testCalculateForOverTwoYearsOldCustomer() {
        List<UserType> userTypes = List.of(UserType.CUSTOMER);
        User user = new User(1L, "Test User", userTypes, LocalDateTime.of(2020, 1, 1, 0, 0));

        List<Item> items = List.of(
                new Item(1L, "Toaster", "Toaster", 1500.0, ItemType.ELECTRONIC),
                new Item(2L, "Corn Flour", "Corn Flour", 500.0, ItemType.GROCERY),
                new Item(3L, "Make-up Kit", "Make-up Kit", 1000.0, ItemType.COSMETIC));

        Discount percentageBasedDiscountForCustomer =
                new Discount(3L,"Loyal Customer Discount", "Percentage based discount for store loyal customers", 5.0, 0.0, 0.0, true, UserType.CUSTOMER);
        Discount billAmountBasedDiscountForCustomer =
                new Discount(4L,"Bill Amount Based Discount", "Bill amount based discount for store customers", 0.0, 5.0, 100.0, false, UserType.CUSTOMER);

        when(discountRepository.findByDiscountUserTypeAndIsPercentageBasedDiscount(UserType.CUSTOMER, Boolean.TRUE)).thenReturn(percentageBasedDiscountForCustomer);
        when(discountRepository.findByDiscountUserTypeAndIsPercentageBasedDiscount(UserType.CUSTOMER, Boolean.FALSE)).thenReturn(billAmountBasedDiscountForCustomer);

        when(userService.isOverTwoYearsOldCustomer(user)).thenReturn(Boolean.TRUE);

        Double netPayableAmount = discountService.calculateDiscount(new BillRequest(user, items));

        assertEquals(2725, netPayableAmount);
    }

    @Test
    public void testCalculateForLessThanTwoYearsOldCustomer() {
        List<UserType> userTypes = List.of(UserType.CUSTOMER);
        User user = new User(1L, "Test User", userTypes, LocalDateTime.of(2024, 1, 1, 0, 0));

        List<Item> items = List.of(
                new Item(1L, "Toaster", "Toaster", 1500.0, ItemType.ELECTRONIC),
                new Item(2L, "Corn Flour", "Corn Flour", 500.0, ItemType.GROCERY),
                new Item(3L, "Make-up Kit", "Make-up Kit", 1000.0, ItemType.COSMETIC));

        Discount billAmountBasedDiscountForCustomer =
                new Discount(4L,"Bill Amount Based Discount", "Bill amount based discount for store customers", 0.0, 5.0, 100.0, false, UserType.CUSTOMER);

        when(discountRepository.findByDiscountUserTypeAndIsPercentageBasedDiscount(UserType.CUSTOMER, Boolean.FALSE)).thenReturn(billAmountBasedDiscountForCustomer);

        when(userService.isOverTwoYearsOldCustomer(user)).thenReturn(Boolean.FALSE);

        Double netPayableAmount = discountService.calculateDiscount(new BillRequest(user, items));

        assertEquals(2850, netPayableAmount);
    }

    @Test
    public void testCalculateBillAmountBasedDiscount() {
        List<UserType> userTypes = List.of(UserType.CUSTOMER);
        User user = new User(1L, "Test User", userTypes, LocalDateTime.now());

        List<Item> items = List.of(
                new Item(1L, "Toaster", "Toaster", 1500.0, ItemType.ELECTRONIC),
                new Item(2L, "Corn Flour", "Corn Flour", 500.0, ItemType.GROCERY),
                new Item(3L, "Make-up Kit", "Make-up Kit", 1000.0, ItemType.COSMETIC));

        Discount billAmountBasedDiscountForCustomer =
                new Discount(4L,"Bill Amount Based Discount", "Bill amount based discount for store customers", 0.0, 5.0, 100.0, false, UserType.CUSTOMER);

        when(discountRepository.findByDiscountUserTypeAndIsPercentageBasedDiscount(UserType.CUSTOMER, Boolean.FALSE)).thenReturn(billAmountBasedDiscountForCustomer);

        when(userService.isOverTwoYearsOldCustomer(user)).thenReturn(Boolean.FALSE);

        Double netPayableAmount = discountService.calculateDiscount(new BillRequest(user, items));

        assertEquals(2850, netPayableAmount);
    }

    @Test
    public void testCalculatePercentageBasedDiscountForGroceryItems() {
        List<UserType> userTypes = List.of(UserType.CUSTOMER, UserType.EMPLOYEE);
        User user = new User(1L, "Test User", userTypes, LocalDateTime.now());

        List<Item> items = List.of(
                new Item(1L, "Grocery Item 1", "Grocery Item 1", 1500.0, ItemType.GROCERY),
                new Item(2L, "Grocery Item 2", "Grocery Item 2", 500.0, ItemType.GROCERY),
                new Item(3L, "Grocery Item 3", "Grocery Item 3", 1000.0, ItemType.GROCERY));

        Discount billAmountBasedDiscountForCustomer =
                new Discount(4L,"Bill Amount Based Discount", "Bill amount based discount for store customers", 0.0, 5.0, 100.0, false, UserType.CUSTOMER);

        when(discountRepository.findByDiscountUserTypeAndIsPercentageBasedDiscount(UserType.CUSTOMER, Boolean.FALSE)).thenReturn(billAmountBasedDiscountForCustomer);

        Double netPayableAmount = discountService.calculateDiscount(new BillRequest(user, items));

        assertEquals(2850, netPayableAmount);
    }
}
