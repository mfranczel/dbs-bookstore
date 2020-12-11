package com.packagename.dbs.view;

import com.packagename.dbs.model.*;
import com.packagename.dbs.model.products.*;
import com.packagename.dbs.service.*;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.ItemClickEvent;
import com.vaadin.flow.component.grid.contextmenu.GridContextMenu;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.NativeButtonRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;
import javax.persistence.Access;
import java.sql.Date;
import java.util.*;


@Route("")
@UIScope
@SpringComponent
public class MainUserView extends AppLayout{

    private BookService bookService;
    private EmployeeService employeeService;
    private StoreService storeService;
    private StatsService statService;
    private AccessoryService accessoryService;
    private OrderService orderService;


    private String role;
    private ArrayList<ArrayList<Object>> cart;
    private Authentication auth;

    private Grid<Employee> employeeGrid;
    private Grid<CompanyStat> grid_stats;
    private Grid<BookORM> bookGrid;
    private Grid<Accessory> accGrid;
    private Grid<Order> ordersGrid;


    Button logoutButton;
    Button newEmployeeButton;
    Button showMyProfileButton;
    Button greaterThanButton;
    Button smallerThanButton;
    Button OverallStatsButton;
    Button CityStatsButton;
    Button BookStatsButton;

    Button loadNextEmployeesBtn;
    Button loadPrevEmployeesBtn;
    Button loadAllEmployeesBtn;


    //order buttons
    private Select<String> valueSelect;
    private Select<String> stateSelect;

    private VerticalLayout vl;
    private HorizontalLayout footer;
    private HorizontalLayout hl;
    private HorizontalLayout hl2;
    private TextField search_firstName;
    private TextField search_lastName;
    private TextField employee_count;

    private Tabs tabs;
    private Tab employee_tab;
    private Map<Tab, Grid> tabsToGrid;
    private Map<Tab, HorizontalLayout> tabsToVerticalLayout;
    private Map<Tab, HorizontalLayout> tabsToVerticalFooter;
    private Map<Tab, HorizontalLayout> selects;


    private TextField searchBook;
    private Button title;
    private String userStore;

    @Autowired
    public MainUserView(OrderService orderService, BookService bookService, AccessoryService accessoryService) {

        // services, which use hibernate data are autowired
        this.bookService = bookService;
        employeeService = new EmployeeService();
        storeService = new StoreService();
        statService = new StatsService();
        this.accessoryService = accessoryService;
        this.orderService = orderService;

        // shopping cart is initialized empty
        cart = new ArrayList<ArrayList<Object>>();

        tabsToGrid = new HashMap<>();
        tabsToVerticalLayout = new HashMap<>();
        tabsToVerticalFooter = new HashMap<>();

        auth = SecurityContextHolder.getContext().getAuthentication();
        String role = "";
        if(auth != null) {
            role = auth.getAuthorities().toArray()[0].toString();
        }
        this.role = role;

        this.setupEmployeeLayout();
        this.setEmployeeFilters();
        this.setUpEmployeeButtons();

        String store = this.storeService.getStoreByEmployeeId(((Account)auth.getPrincipal()).getEmployee_id()).getStreet();
        this.userStore = store;

        employeeService.getCount();
        if(role.equals("ROLE_ADMIN")) {
            tabsToVerticalFooter.put( employee_tab, footer); //has to be after setupEmployeeLayout()
            this.expandedTabOptions();
            setUpFooter();
            employeeGrid.setItems(employeeService.getNext());
            hl.add(search_firstName, search_lastName, newEmployeeButton);
            hl2.setVerticalComponentAlignment(FlexComponent.Alignment.END, newEmployeeButton);

        } else {
            employeeGrid.setItems(employeeService.getAllFromStore(store));
        }

        this.addBooksTab();

        vl.add(hl);
        vl.add(employeeGrid);

        vl.add(footer);
        setContent(vl);


    }

    private void setEmployeeFilters(){

        search_firstName = new TextField();
        search_lastName = new TextField();

        search_firstName.setLabel("Filter by first name");
        search_firstName.setPlaceholder("Search");
        search_firstName.setReadOnly(false);
        search_firstName.setMaxLength(50);
        search_firstName.addValueChangeListener(e -> {
            employeeGrid.setItems(employeeService.filterByFirstName(e.getValue()));
            search_lastName.setValue("");
        });
        search_firstName.setValueChangeMode(ValueChangeMode.EAGER);

        search_lastName.setLabel("Filter by last name");
        search_lastName.setPlaceholder("Search");
        search_lastName.setReadOnly(false);
        search_lastName.setMaxLength(50);
        search_lastName.addValueChangeListener(e -> {
            employeeGrid.setItems(employeeService.filterByLastName(e.getValue()));
            search_firstName.setValue("");
        });
        search_lastName.setValueChangeMode(ValueChangeMode.EAGER);
        addToNavbar(new DrawerToggle(),  hl2);
    }


    private void setStatsFilters() {
        employee_count = new TextField();
        employee_count.setLabel("Filter employees (count)");
        employee_count.setPlaceholder("Search");
        employee_count.setReadOnly(false);
        employee_count.setMaxLength(4);
        employee_count.setValueChangeMode(ValueChangeMode.EAGER);

        greaterThanButton = new Button("Show greater than");
        smallerThanButton = new Button("Show smaller than");
        OverallStatsButton = new Button("Overall stats");
        CityStatsButton = new Button("City stats");
        BookStatsButton = new Button( "Book stats" );

        greaterThanButton.addClickListener(e -> grid_stats.setItems(statService.greaterThan(employee_count.getValue())));
        smallerThanButton.addClickListener(e -> grid_stats.setItems(statService.smallerThan(employee_count.getValue())));
        OverallStatsButton.addClickListener(e -> showStats());
        CityStatsButton.addClickListener(e -> showBranchesPerCity());
        BookStatsButton.addClickListener( e -> showBookStats());
    }

    private void setOrdersButtons(){
        this.valueSelect = new Select<>();
        this.stateSelect = new Select<>();

        valueSelect.setItems("Orders from my store", "All orders");
        valueSelect.setValue("Orders from my store");

        valueSelect.addValueChangeListener(e -> {
            String gll = e.getValue();

            if(gll.equals("Orders from my store")){

                if(stateSelect.getValue().equals("All")) {
                    this.ordersGrid.setItems(this.orderService.getAllFromStore(this.userStore));
                } else {
                    this.ordersGrid.setItems(this.orderService.getAllByStateByStore(stateSelect.getValue(), this.userStore));
                }

            } else {
                this.ordersGrid.setItems(this.orderService.getAll());

                if(stateSelect.getValue().equals("All")) {
                    this.ordersGrid.setItems(this.orderService.getAll());
                } else {
                    this.ordersGrid.setItems(this.orderService.getAllByState(stateSelect.getValue()));
                }
            }

        });


        stateSelect.setItems("All", "Sent", "Accepted", "Rejected", "Delivered");

        stateSelect.setValue("All");

        stateSelect.addValueChangeListener(e -> {
            String gll = e.getValue();

            if(gll.equals("All")) {
                if(valueSelect.getValue().equals("Orders from my store")) {
                    this.ordersGrid.setItems(this.orderService.getAllFromStore(this.userStore));
                } else {
                    this.ordersGrid.setItems(this.orderService.getAll());
                }
            } else {
                if(valueSelect.getValue().equals("Orders from my store")) {
                    this.ordersGrid.setItems(this.orderService.getAllByStateByStore(gll, this.userStore));
                } else {
                    this.ordersGrid.setItems(this.orderService.getAllByState(gll));
                }
            }
        });




    }

    private void setUpEmployeeButtons(){
        logoutButton = new Button("Log out");
        newEmployeeButton = new Button("New Employee");
        showMyProfileButton = new Button("My profile");

        logoutButton.getElement().getStyle().set("margin-left", "auto");
        logoutButton.addClickListener(e ->{
            SecurityContextHolder.clearContext();
            VaadinSession.getCurrent().getSession().invalidate();
            UI.getCurrent().getPage().executeJs("window.location.href='/logout'");
        });
        newEmployeeButton.addClickListener(e -> addNewEmployee());
        showMyProfileButton.addClickListener(e -> showMyProfile());

        hl2.add(logoutButton);
        hl2.add(showMyProfileButton);
    }

    private void setupEmployeeLayout() {
        vl = new VerticalLayout();
        hl = new HorizontalLayout();
        hl2 = new HorizontalLayout();
        footer = new HorizontalLayout();

        setupEmployeeGrid();
        setupTabs();

        hl2.setAlignItems(FlexComponent.Alignment.END);
        hl2.setWidth("100%");
    }

    private void setupTabs() {

        employee_tab = new Tab("Employees");

        tabsToGrid.put(employee_tab, employeeGrid);
        tabsToVerticalLayout.put(employee_tab, hl);
        tabsToVerticalFooter.put( employee_tab, footer);

        tabs = new Tabs(employee_tab/*, tab3*/);
        tabs.setOrientation(Tabs.Orientation.VERTICAL);
        tabs.getSelectedTab().add(employeeGrid);
        addToDrawer(tabs);

        tabs.addSelectedChangeListener(e->{
            vl.removeAll();
            vl.add(tabsToVerticalLayout.get(tabs.getSelectedTab()));
            vl.add(tabsToGrid.get(tabs.getSelectedTab()));
            try {
                vl.add(tabsToVerticalFooter.get(tabs.getSelectedTab()));
            }catch (Exception e1){}
        });
    }


    private void expandedTabOptions() {
        setOrdersButtons();
        setupStatsGrid();
        setupOrdersGrid();
        setStatsFilters();
        setOrdersButtons();

        HorizontalLayout hl_stats = new HorizontalLayout();
        HorizontalLayout hl_orders = new HorizontalLayout();

        hl_stats.add(employee_count);
        hl_stats.add(greaterThanButton);
        hl_stats.add(smallerThanButton);
        hl_stats.add(OverallStatsButton);
        hl_stats.add(CityStatsButton);
        hl_stats.add(BookStatsButton);

        hl_orders.add(valueSelect);
        hl_orders.add(stateSelect);

        Tab stats_tab = new Tab("Stats");
        Tab orders_tab = new Tab("Orders");

        hl_stats.setVerticalComponentAlignment(FlexComponent.Alignment.END, employee_count, greaterThanButton, smallerThanButton, OverallStatsButton, CityStatsButton, BookStatsButton);
        tabsToGrid.put(stats_tab, grid_stats);
        tabsToGrid.put(orders_tab, ordersGrid);

        tabsToVerticalLayout.put(stats_tab, hl_stats);
        tabsToVerticalLayout.put(orders_tab, hl_orders);

        tabs.add(stats_tab);
        tabs.add(orders_tab);
    }

    private void showOtherProducts(){
        vl.removeAll();
        accGrid = new Grid<>(Accessory.class);
        accGrid.setColumns("name", "type", "manufacturerName");
        accGrid.setMinHeight("85vh");
        accGrid.setItems(accessoryService.getAll());
        accGrid.addItemClickListener(e -> showAccessoryDetails(e));
        Button button = new Button("Show Books");
        Button showCart = new Button("Show Cart");
        showCart.setMinWidth("20");
        showCart.addClickListener(e -> this.showCart());

        HorizontalLayout hrl = new HorizontalLayout();


        GridContextMenu<Product> productMenu = new GridContextMenu();
        productMenu.addItem("Add to cart", e -> {
            Product selectedProduct = e.getItem().get();

            if(selectedProduct instanceof Accessory) {
                this.showManufacturers((Accessory)selectedProduct);
            }


        });
        if(role.equals("ROLE_ADMIN")) {
            productMenu.setTarget(this.accGrid);
        }
        button.addClickListener(e -> {
            vl.removeAll();
            HorizontalLayout hl = new HorizontalLayout();
            addBookButtons(hl);
            vl.add(hl, this.bookGrid);

        });
        hrl.add(button);
        hrl.add(showCart);
        hrl.setAlignItems(FlexComponent.Alignment.END);


        vl.add(hrl);
        vl.add(accGrid);

        
    }

    private void addBookButtons(HorizontalLayout hl){
        Button otherProductsButton = new Button("Show Accessories");
        hl.add(otherProductsButton);
        otherProductsButton.addClickListener(e -> {
            this.showOtherProducts();
        });
        //adds button and search bar
        filterBooks(hl);
    }

    private void showManufacturers(Accessory accessory) {

        Dialog dialog = new Dialog();
        VerticalLayout vl = new VerticalLayout();
        vl.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);

        Label header1 = new Label("Add to shopping cart");
        header1.getStyle().set("font-size", "25px");
        header1.setWidth("100%");
        vl.add(header1);

        Accessory acc1 = accessoryService.getWholeAccInfo(accessory);

        HorizontalLayout hl = new HorizontalLayout();

        TextField name = new TextField();
        name.setValue(acc1.getName());
        name.setReadOnly(true);

        TextField manufacturer = new TextField();
        manufacturer.setValue(acc1.getManufacturer().getName());
        manufacturer.setReadOnly(true);

        IntegerField integerField = new IntegerField();
        integerField.setPlaceholder("Quantity");

        hl.add(name, manufacturer, integerField);

        vl.add(hl);

        Button submitButton = new Button("Add to cart");

        submitButton.addClickListener(e -> {
            if(integerField.getValue() != null) {
                ArrayList<Object> item = new ArrayList<>();
                item.add(acc1);
                item.add(acc1.getManufacturer());
                item.add(integerField.getValue());
                this.cart.add(item);
                dialog.close();
            }
        });
        vl.add(submitButton);

        dialog.add(vl);

        dialog.open();

    }

    private void showPublishers(BookORM selectedBook){
        Dialog dialog = new Dialog();

        VerticalLayout vl = new VerticalLayout();
        vl.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        Label header1 = new Label("Add to shopping cart");
        header1.getStyle().set("font-size", "25px");
        header1.setWidth("100%");
        vl.add(header1);

        //HashMap<Checkbox, Manufacturer> checkboxes = new HashMap<>();
        ArrayList<BookCopy> copies = new ArrayList<>();
        copies.addAll(this.bookService.getAllCopies(selectedBook.getId()));
        ArrayList<ArrayList<Object>> rows = new ArrayList<>();

        for(BookCopy b: copies){
            HorizontalLayout hl = new HorizontalLayout();

            TextField name = new TextField();
            name.setValue(b.getManufacturer().getName());
            name.setReadOnly(true);

            Checkbox chckbox = new Checkbox();

            ArrayList<Object> arr = new ArrayList<>();
            arr.add(chckbox);
            arr.add(b.getManufacturer());

            IntegerField integerField = new IntegerField();
            integerField.setPlaceholder("Quantity");
            arr.add(integerField);
            arr.add(b);
            rows.add(arr);

            hl.add(name, chckbox, integerField);
            hl.setVerticalComponentAlignment(FlexComponent.Alignment.CENTER, chckbox);

            vl.add(hl);
        }

        Button submitButton = new Button("Submit");
        submitButton.addClickListener(e -> {


            for (ArrayList<Object> ar: rows){

                Checkbox c = (Checkbox) ar.get(0);
                if(c.getValue() && ((IntegerField)ar.get(2)).getValue() != null) {
                    ArrayList<Object> arr = new ArrayList<>();
                    arr.add(ar.get(3));
                    arr.add(ar.get(1));
                    arr.add(((IntegerField)ar.get(2)).getValue());
                    this.cart.add(arr);
                }
            }

            dialog.close();
        });

        if(copies.size() == 0) {
            Label text = new Label("Nothing to add...");
            text.getStyle().set("font-size", "16px");
            vl.add(text);
        }
        vl.add(submitButton);

        dialog.add(vl);
        dialog.setHeight("450px");
        dialog.setWidth("400px");
        dialog.open();
    }

    private void addBooksTab(){
        setUpBooksGrid();
        HorizontalLayout hl = new HorizontalLayout();
        addBookButtons(hl);

        GridContextMenu<BookORM> productMenu = new GridContextMenu();
        productMenu.addItem("Add to cart", e -> {
            BookORM selectedProduct = e.getItem().get();

            this.showPublishers(selectedProduct);


        });
        if(role.equals("ROLE_ADMIN")) {
            productMenu.setTarget(this.bookGrid);
        }
        Tab productsTab = new Tab("Products");
        tabsToGrid.put(productsTab, bookGrid);
        tabsToVerticalLayout.put(productsTab, hl);
        tabs.add(productsTab);

    }

    private void filterBooks(HorizontalLayout hl){

        hl.setAlignItems(FlexComponent.Alignment.END);
        searchBook = new TextField();
        searchBook.setLabel("Enter value: ");
        searchBook.setPlaceholder("Search");
        searchBook.setReadOnly(false);
        searchBook.setMaxLength(100);
        searchBook.setMinWidth( "20" );


        Label record = new Label( "" );
        ComboBox<String> rangeComboBox = new ComboBox<>();

        //this is far from ideal, hardcoded
        ArrayList<String> modes = new ArrayList<>();
        modes.add("Search by Name");
        modes.add("Search by Author");
        modes.add("Search by Year");
        modes.add("Search by ISBN");
        modes.add("Search by issuer");
        modes.add("Show on Stock");
        modes.add("Show not on Stock");

        ComboBox<String> modeComboBox = new ComboBox<>();
        modeComboBox.setItems(modes);
        modeComboBox.setLabel( "Select search ");
        modeComboBox.setValue("Search by Name");


        modeComboBox.addValueChangeListener(valueChangeEvent -> {
            if (valueChangeEvent.getValue() != null) {
                if (modes.indexOf(valueChangeEvent.getValue()) == 2) {//this is far from ideal, hardcoded
                    rangeComboBox.setEnabled( true );
                    searchBook.setEnabled(true);
                }else if(modes.indexOf(valueChangeEvent.getValue()) == 5){
                    rangeComboBox.setValue("");
                    rangeComboBox.setEnabled( false );
                    searchBook.setValue("");
                    searchBook.setEnabled(false);
                }else if(modes.indexOf(valueChangeEvent.getValue()) == 6){
                    rangeComboBox.setValue("");
                    rangeComboBox.setEnabled( false );
                    searchBook.setValue("");
                    searchBook.setEnabled(false);
                }else{
                    rangeComboBox.setValue("");
                    rangeComboBox.setEnabled( false );
                    searchBook.setEnabled(true);
                }
            }
        });


        rangeComboBox.setItems("0", "1", "2", "3", "5", "10", "25", "50", "100");
        rangeComboBox.setLabel( "Range from current year");
        rangeComboBox.setEnabled( false );


        Button searchBtn = new Button("Search");
        searchBtn.addClickListener(e ->{
            //bookGrid.setItems(service.choice(modes.indexOf(modeComboBox.getValue()), rangeComboBox.getValue(), searchBook.getValue()));
            if (modes.indexOf(modeComboBox.getValue()) == 2 || modes.indexOf(modeComboBox.getValue()) == 3 || modes.indexOf(modeComboBox.getValue()) == 4) {
                bookGrid.setColumns("name", "authorName", "genre", "searchedResults");
            }else {
                bookGrid.setColumns("name", "authorName", "genre");
            }
            bookGrid.setItems(this.bookService.new_choice(modes.indexOf(modeComboBox.getValue()), rangeComboBox.getValue(), searchBook.getValue()));
        });

        Button cart = new Button("Show Cart");
        cart.setMinWidth("20");
        if(role.equals("ROLE_ADMIN")){
            hl.add(cart);
        }
        cart.addClickListener(e -> this.showCart());


        Button prev = new Button("Prev");
        prev.setMinWidth("20");
        prev.addClickListener(e -> bookGrid.setItems(this.bookService.getPrev()));

        Button next = new Button("Next");
        next.setMinWidth("20");
        next.addClickListener(e -> bookGrid.setItems( this.bookService.getNext() ));

        Button all = new Button("GET ALL");
        all.setMinWidth("20");
        all.addClickListener(e ->bookGrid.setItems(this.bookService.getALL()));

        hl.add(modeComboBox);
        hl.add(rangeComboBox);
        hl.add(searchBook);
        hl.add(searchBtn);
        hl.add( record );
        hl.add( prev );
        hl.add( all );
        hl.add( next );

    }

    private void showCart(){

        Dialog dialog = new Dialog();

        VerticalLayout vl = new VerticalLayout();
        Label header1 = new Label("Shopping Cart Contents");
        header1.getStyle().set("font-size", "25px");
        header1.setWidth("100%");
        vl.add(header1);

        if(this.cart.size() == 0) {
            Label text = new Label("Your shopping cart is empty...");
            text.getStyle().set("font-size", "16px");
            vl.add(text);
            vl.setWidth("100%");
            vl.setAlignItems(FlexComponent.Alignment.CENTER);
            text.setWidth("100%");
        } else {

            Label nameL = new Label("Name");
            Label manufacturer = new Label("Manufacturer");
            Label quantityL = new Label("Quantity");

            HorizontalLayout hl2 = new HorizontalLayout();

            nameL.getStyle().set("margin-right","150px");
            manufacturer.getStyle().set("margin-right", "100px");

            hl2.add(nameL);
            hl2.add(manufacturer);
            hl2.add(quantityL);
            vl.add(hl2);

            for(ArrayList<Object> ar: this.cart) {
                HorizontalLayout hl = new HorizontalLayout();

                Product p = (Product) ar.get(0);

                int q = (int) ar.get(2);
                String name;

                TextField manField = new TextField();
                manField.setReadOnly(true);

                if(p instanceof BookCopy) {
                    Manufacturer m = (Manufacturer) ar.get(1);
                    name = ((BookCopy) p).getBook().getName();
                    manField.setValue(m.getName());
                } else {
                    Manufacturer m = (Manufacturer) ar.get(1);
                    name = ((Accessory) p).getName();
                    manField.setValue(m.getName());
                }

                TextField nameField = new TextField();
                nameField.setValue(name);
                nameField.setReadOnly(true);


                IntegerField quantity = new IntegerField();
                quantity.setValue(q);
                quantity.setEnabled(false);

                hl.add(nameField);
                hl.add(manField);
                hl.add(quantity);


                vl.add(hl);
            }
            Button clearButton = new Button("Clear");
            clearButton.addClickListener(e -> {this.cart.clear();});
            Button submitButton = new Button("Submit");
            submitButton.addClickListener(e -> {
                this.orderService.sendOrders(this.storeService.getStoreByEmployeeId(((Account)auth.getPrincipal()).getEmployee_id()), this.cart);
                dialog.close();
            });




            HorizontalLayout hhl = new HorizontalLayout();
            hhl.add(clearButton, submitButton);
            vl.add(hhl);

        }


        dialog.add(vl);
        dialog.open();
    }

    private void showTransferDialog(String isbn, String storeStreet, String city, int max){

        Dialog dialog = new Dialog();

        VerticalLayout vl = new VerticalLayout();
        vl.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);

        TextField fromStore = new TextField("Transfer source", storeStreet+", "+city,"");
        fromStore.setReadOnly(true);
        fromStore.setWidth("80%");
        ComboBox<String> toStoreCombo = new ComboBox<>();
        toStoreCombo.setLabel("Transfer destination");
        toStoreCombo.setItems(this.storeService.getAllStoreNames());
        toStoreCombo.setRequired(true);
        toStoreCombo.setWidth("80%");

        TextField selectedBook = new TextField("Selected book",isbn,"");
        selectedBook.setEnabled(false);
        selectedBook.setWidth("80%");

        NumberField numberField = new NumberField("No. of books (1<" + max + ")");
        numberField.setValue(1d);
        numberField.setHasControls(true);
        numberField.setMin(1);
        numberField.setMax(max);

        Button transfer = new Button("Transfer");

        transfer.addClickListener(e -> {
            this.bookService.transferBooks(storeStreet, toStoreCombo.getValue().split(", ")[1],
                    numberField.getValue(), isbn);
            dialog.close();
        });


        vl.add(fromStore);
        vl.add(toStoreCombo);
        vl.add(selectedBook);
        vl.add(numberField);
        vl.add(transfer);

        dialog.add(vl);

        dialog.setHeight("450px");
        dialog.setWidth("600px");
        dialog.open();

    }


    private void setupOrdersGrid() {
        ordersGrid = new Grid<>(Order.class);
        ordersGrid.setColumns("date", "storeAddress", "manufacturer", "state", "numberOfProducts");
        ordersGrid.setMinHeight("85vh");
        ordersGrid.addColumn(new ComponentRenderer<>(order -> {
            Select<String> valueSelect = new Select<String>();
            valueSelect.setItems("Sent", "Accepted", "Rejected", "Delivered");
            valueSelect.setEmptySelectionAllowed(false);
            valueSelect.setValue(order.getState());
            if(valueSelect.getValue().equals("Delivered")) {
                valueSelect.setEnabled(false);
            }

            valueSelect.addValueChangeListener(e -> {
                this.orderService.changeState(order, e.getValue());
                if(e.getValue().equals("Delivered")) {
                    this.orderService.deliver(order);
                    valueSelect.setEnabled(false);
                }
            });

            return valueSelect;
        })).setHeader("Set state");
        ordersGrid.setItems(this.orderService.getAllFromStore(this.userStore));
        ordersGrid.addItemClickListener(e -> showOrderDetails(e));
    }


    private void setupStatsGrid() {
        grid_stats = new Grid<>(CompanyStat.class);
        grid_stats.setColumns("street", "city", "num_of_employees");
        grid_stats.setMinHeight("85vh");
        grid_stats.setItems(statService.getAll());
        grid_stats.addItemClickListener(e -> showBranchDetail(e));
    }

    private void setUpBooksGrid(){
        bookGrid = new Grid<>(BookORM.class);
        bookGrid.setColumns("name", "authorName", "genre");
        bookGrid.setMinHeight("85vh");
        bookGrid.setItems(bookService.getAllBooks());
        bookGrid.addItemClickListener(e -> showBookDetails(e));
    }

    private void setupEmployeeGrid() {
        employeeGrid = new Grid<>(Employee.class);
        employeeGrid.setColumns("first", "last", "storeStreet");
        employeeGrid.setMinHeight("80vh");
        employeeGrid.setItems(employeeService.getAll());
        employeeGrid.addItemClickListener(e -> showEmployeeDetails(e));
    }


    private void showAccessoryDetails(ItemClickEvent e) {
        Accessory a = (Accessory)e.getItem();

        Dialog dialog = new Dialog();
        VerticalLayout vl = new VerticalLayout();
        vl.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        dialog.add(vl);

        Accessory updatedAcc = this.accessoryService.getWholeAccInfo(a);

        Label header1 = new Label("Accessory Information");

        TextField acc_name = new TextField("Name",updatedAcc.getName(),"");
        TextField type = new TextField("Type",updatedAcc.getType(),"");
        TextField manu = new TextField("Author",updatedAcc.getManufacturer().getName(),"");

        Label header2 = new Label("Availability Information");
        Label store = new Label("Store: " + updatedAcc.getStore());
        Label available = new Label("Available: " + updatedAcc.getIn_stock());

        acc_name.setWidth("75%");
        type.setWidth("75%");
        manu.setWidth("75%");
        header1.setWidth("75%");
        store.setWidth("75%");
        available.setWidth("75%");
        header2.setWidth("75%");
        acc_name.setReadOnly(true);
        type.setReadOnly(true);
        manu.setReadOnly(true);
        header1.getStyle().set("font-size", "25px");
        header2.getStyle().set("font-size", "25px");

        vl.add(header1);
        vl.add(acc_name);
        vl.add(type);
        vl.add(manu);
        vl.add(header2);
        vl.add(store);
        vl.add(available);

        dialog.setWidth("600px");
        dialog.setHeight("500px");

        dialog.open();


    }

    private void showBookDetails(ItemClickEvent e) {
        BookORM book = ((BookORM)e.getItem());

        Dialog dialog = new Dialog();
        VerticalLayout vl = new VerticalLayout();
        vl.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        dialog.add(vl);

        ArrayList<BookCopy> copies = (ArrayList<BookCopy>) this.bookService.getAllCopies(book.getId());

        Label header1 = new Label("General Book Information");

        TextField book_name = new TextField("Title",book.getName(),"");
        TextField genre = new TextField("Genre",book.getGenre(),"");
        TextField author = new TextField("Author",book.getAuthorName(),"");

        book_name.setWidth("75%");
        genre.setWidth("75%");
        author.setWidth("75%");
        header1.setWidth("75%");
        book_name.setReadOnly(true);
        genre.setReadOnly(true);
        author.setReadOnly(true);

        Label header2 = new Label("Additional Availability Information");
        header2.setWidth("75%");
        header1.getStyle().set("font-size", "25px");
        header2.getStyle().set("font-size", "25px");
        vl.add(header1);
        vl.add(book_name);
        vl.add(genre);
        vl.add(author);
        vl.add(header2);

        int index = 0;
        Label isbn_t;
        Label manufacturer;
        Label publicationDate;
        Label store;
        Label available;

        for (BookCopy cp: copies) {
            String isbn_s = (String) cp.getIsbn();

            isbn_t = new Label("ISBN: " + isbn_s);
            manufacturer = new Label("Publisher: " + cp.getManufacturer().getName());
            publicationDate = new Label("Publication date: " + cp.getDayOfPublication().toString());
            store = new Label("Store: " +
                    cp.getStore().getStreet() + ", " +
                    cp.getStore().getCity());
            available = new Label("Available: " + cp.getIn_stock());

            vl.add(isbn_t);
            vl.add(manufacturer);
            vl.add(publicationDate);
            vl.add(store);
            vl.add(available);

            isbn_t.setWidth("75%");
            manufacturer.setWidth("75%");
            publicationDate.setWidth("75%");
            store.setWidth("75%");
            available.setWidth("75%");

            String store_street = cp.getStore().getStreet();
            String store_city = cp.getStore().getCity();
            int available_i = cp.getIn_stock();
            Button transferButton = new Button("Transfer books");
            vl.add(transferButton);
            transferButton.addClickListener(el -> {
                this.showTransferDialog(isbn_s, store_street, store_city, available_i);
            });

            if (index + 1 < copies.size()) {
                Hr hr = new Hr();
                hr.setWidth("75%");
                hr.getStyle().set("margin", "auto");
                vl.add(hr);
            }


            index++;
        }


        dialog.setWidth("600px");
        dialog.setHeight("800px");
        dialog.open();

    }


    private void setUpFooter() {

        loadNextEmployeesBtn = new Button( "prev");
        loadNextEmployeesBtn.addClickListener(e -> {
            employeeGrid.setItems(employeeService.getPrev());
        });
        footer.add(loadNextEmployeesBtn);

        loadAllEmployeesBtn = new Button( "GET ALL");
        loadAllEmployeesBtn.addClickListener(e -> {
            employeeGrid.setItems(employeeService.getAll());
        });
        footer.add(loadAllEmployeesBtn);

        loadNextEmployeesBtn = new Button( "next");
        loadNextEmployeesBtn.addClickListener(e -> {
            employeeGrid.setItems(employeeService.getNext());
        });
        footer.add(loadNextEmployeesBtn);
    }

    private void showEmployeeDetails(ItemClickEvent<Employee> e) {

        Dialog dialog = new Dialog();
        VerticalLayout vl = new VerticalLayout();
        vl.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        dialog.add(vl);
      
        Button removeButton = new Button("Remove");
        Account acc = this.employeeService.getAccountByEmployee(e.getItem());

        TextField f_name = new TextField();
        f_name.setValue((e.getItem()).getFirst());
        f_name.setLabel("First name");
        f_name.setReadOnly(true);
        f_name.setWidth("80%");

        TextField l_name = new TextField();
        l_name.setValue((e.getItem()).getLast());
        l_name.setLabel("Last name");
        l_name.setReadOnly(true);
        l_name.setWidth("80%");

        TextField company_role = new TextField();
        if (acc != null) {
            company_role.setValue(acc.getCompany_role());
            company_role.setLabel("Company role");
            company_role.setReadOnly(true);
            company_role.setWidth("80%");
        }

        TextField workplace = new TextField();
        workplace.setValue((e.getItem()).getStoreStreet());
        workplace.setLabel("Workplace");
        workplace.setReadOnly(true);
        workplace.setWidth("80%");

        removeButton.addClickListener(event -> {
            dialog.close();
            this.showRemoveDialog((Employee)e.getItem(), acc);
        });


        vl.add(f_name);
        vl.add(l_name);
        if (acc != null) {
            vl.add(company_role);
        }
        vl.add(workplace);
        if(this.role.equals("ROLE_ADMIN")) {
            vl.add(removeButton);
        }
        dialog.setWidth("400px");
        dialog.setHeight("500px");
        dialog.open();
    }

    private void showRemoveDialog(Employee employee, Account account){
        Dialog remDialog = new Dialog();
        VerticalLayout vl = new VerticalLayout();
        vl.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        remDialog.add(vl);

        Button confirmButton = new Button("Remove");
        ArrayList<String> modes = new ArrayList<>();

        modes.add("Remove account");
        modes.add("Disable account");
        modes.add("Remove account and employee info.");
        Label label = new Label();

        ComboBox<String> removalMode = new ComboBox<>();
        removalMode.setItems(modes);
        removalMode.setRequired(true);
        removalMode.setWidth("80%");
        confirmButton.addClickListener(e -> {
            label.setText("");
            if (removalMode.getValue().equals(modes.get(0))){
                boolean success = this.employeeService.removeAccount(account);
                if(success) {
                    remDialog.close();
                } else {
                    label.setText("Error, account may not exist.");
                }
            } else if (removalMode.getValue().equals(modes.get(1))){
                boolean success = this.employeeService.disableAccount(account);
                if(success) {
                    remDialog.close();
                } else {
                    label.setText("Error, account may not exist.");
                }
            } else if (removalMode.getValue().equals(modes.get(2))){
                this.employeeService.removeEmployee(employee, account);
                remDialog.close();
            }
        });

        vl.add(removalMode);
        vl.add(confirmButton);
        vl.add(label);

        remDialog.setWidth("400px");
        remDialog.setHeight("250px");
        remDialog.open();
    }

    private void addNewEmployee() {
        Dialog dialog = new Dialog();
        VerticalLayout vl = new VerticalLayout();
        vl.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        dialog.add(vl);

        TextField f_name = new TextField();
        f_name.setRequired(true);
        f_name.setLabel("First name");
        f_name.setWidth("80%");
        f_name.setMinLength(2);
        TextField l_name = new TextField();
        l_name.setLabel("Last name");
        l_name.setRequired(true);
        l_name.setMinLength(2);
        l_name.setWidth("80%");
        TextField mail = new TextField();
        mail.setLabel("Mail");
        mail.setRequired(true);
        mail.setMinLength(2);
        mail.setWidth("80%");
        TextField phone = new TextField();
        phone.setLabel("Phone number");
        phone.setRequired(true);
        phone.setMinLength(2);
        phone.setWidth("80%");

        TextField address = new TextField();
        address.setLabel("Address");
        address.setRequired(true);
        address.setWidth("80%");
        mail.setLabel("Address");
        mail.setRequired(true);
        mail.setMinLength(10);
        mail.setWidth("80%");
        DatePicker birthday = new DatePicker();
        birthday.setLabel("Birth date");
        birthday.setWidth("80%");

        ComboBox<String> companyRole = new ComboBox<>();
        companyRole.setItems(this.employeeService.getAllRoles());
        companyRole.setWidth("80%");
        companyRole.setRequired(true);
        companyRole.setLabel("Company role");

        ComboBox<String> labelComboBox = new ComboBox<>();
        labelComboBox.setWidth("80%");
        labelComboBox.setItems(this.storeService.getAllStoreNames());
        labelComboBox.setRequiredIndicatorVisible(true);
        labelComboBox.setLabel("Store");

        TextField username = new TextField();
        username.setWidth("80%");
        username.setMinLength(5);
        username.setLabel("Username");
        username.setRequired(true);
        TextField password = new TextField();
        password.setWidth("80%");
        password.setMinLength(8);
        password.setRequired(true);
        password.setLabel("Password");

        Button confirm = new Button("Confirm");
        confirm.addClickListener(e -> {
            this.employeeService.createEmployeeWithAccount(f_name.getValue(), l_name.getValue(), companyRole.getValue(),
                    labelComboBox.getValue().split(", ")[1], username.getValue(), password.getValue(), mail.getValue(), phone.getValue(),
                    birthday.getValue(), address.getValue());
            dialog.close();
        });

        vl.add(f_name);
        vl.add(l_name);
        vl.add(mail);
        vl.add(phone);
        vl.add(birthday);
        vl.add(address);

        vl.add(companyRole);
        vl.add(labelComboBox);
        vl.add(username);
        vl.add(password);
        vl.add(confirm);
        dialog.setWidth("30vw");
        dialog.setHeight("65vh");
        dialog.open();
    }

    private void showMyProfile(){
        Dialog dialog = new Dialog();
        VerticalLayout vl = new VerticalLayout();
        vl.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        dialog.add(vl);

        Account loggedInEmployee = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Employee emp = this.employeeService.getEmployeeByAccount(loggedInEmployee);

        TextField f_name = new TextField();
        f_name.setLabel("First name");
        f_name.setWidth("80%");
        f_name.setValue(emp.getFirst());
        f_name.setReadOnly(true);

        TextField l_name = new TextField();
        l_name.setLabel("Last name");
        l_name.setWidth("80%");
        l_name.setValue(emp.getLast());
        l_name.setReadOnly(true);

        TextField company_role = new TextField();
        company_role.setWidth("80%");
        company_role.setLabel("Company role");
        company_role.setValue(loggedInEmployee.getCompany_role());
        company_role.setReadOnly(true);

        TextField store = new TextField();
        store.setWidth("80%");
        store.setLabel("Store");
        store.setValue(emp.getStoreStreet());
        store.setReadOnly(true);

        TextField username = new TextField();
        username.setWidth("80%");
        username.setLabel("Username");
        username.setValue(loggedInEmployee.getUsername());
        PasswordField pasword = new PasswordField();
        pasword.setWidth("80%");
        pasword.setLabel("Password");
        pasword.setValue(loggedInEmployee.getPassword());

        Button submitButton = new Button("Submit");

        vl.add(f_name);
        vl.add(l_name);
        vl.add(company_role);
        vl.add(store);
        vl.add(username);
        vl.add(pasword);
        vl.add(submitButton);


        submitButton.addClickListener(e -> {
            loggedInEmployee.setPassword(pasword.getValue());
            loggedInEmployee.setUsername(username.getValue());
            this.employeeService.update(loggedInEmployee);
            dialog.close();
        });

        dialog.setWidth("30vw");
        dialog.setHeight("65vh");
        dialog.open();
    }

    private void showStats(){

        Dialog dialog = new Dialog();
        VerticalLayout vl = new VerticalLayout();
        vl.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        dialog.add(vl);

        com.vaadin.flow.component.html.Label label = new com.vaadin.flow.component.html.Label();
        label.setText("Stats data");

        TextField avg_field =  new TextField();
        avg_field.setLabel("Average number of employees per branch");
        avg_field.setWidth("80%");
        avg_field.setValue(statService.getAvg());
        avg_field.setReadOnly(true);

        TextField max_field =  new TextField();
        max_field.setLabel("Largest number of employees per branch");
        max_field.setWidth("80%");
         max_field.setValue(statService.getMax());
        max_field.setReadOnly(true);

        TextField min_field =  new TextField();
        min_field.setLabel("Smallest number of employees per branch");
        min_field.setWidth("80%");
        min_field.setValue(statService.getMin());
        min_field.setReadOnly(true);

        TextField sum_field =  new TextField();
        sum_field.setLabel("Total number of employees");
        sum_field.setWidth("80%");
        sum_field.setValue(statService.getTotal());
        sum_field.setReadOnly(true);

        vl.add(label);
        vl.add(avg_field);
        vl.add(max_field);
        vl.add(min_field);
        vl.add(sum_field);

        dialog.setWidth("30vw");
        dialog.setHeight("65vh");
        dialog.open();
    }

    private void showBranchesPerCity(){

        Dialog dialog = new Dialog();
        VerticalLayout vl = new VerticalLayout();
        vl.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);


        Grid<CompanyStat> local_grid = new Grid<>(CompanyStat.class);
        local_grid.setColumns("city", "num_of_employees");
        local_grid.setMinHeight("80vh");
        local_grid.setItems(statService.getALLEmployeesPerCity());
        local_grid.addItemClickListener(e -> showAllInCity(e));


        TextField lower_bound = new TextField();
        TextField upper_bound = new TextField();
        Button applyButton = new Button("Apply");

        lower_bound.setLabel("Lower bound");
        lower_bound.setPlaceholder("Insert value");
        lower_bound.setReadOnly(false);
        lower_bound.setMaxLength(4);

        upper_bound.setLabel("Upper bound");
        upper_bound.setPlaceholder("Insert value");
        upper_bound.setReadOnly(false);
        upper_bound.setMaxLength(4);

        applyButton.addClickListener(e -> {
            local_grid.setItems( statService.getEmployeesPerCity(lower_bound.getValue(), upper_bound.getValue()) );
        });

        vl.add(lower_bound);
        vl.add(upper_bound);
        vl.add(applyButton);

        vl.add(local_grid);
        dialog.add(vl);
        dialog.setWidth("600px");
        dialog.setHeight("800px");
        dialog.open();
    }

    private void showBookStats(){

        Dialog dialog = new Dialog();
        VerticalLayout vl = new VerticalLayout();
        vl.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);

        Grid<CompanyStat> avg_grid = new Grid<>(CompanyStat.class);
        avg_grid.setColumns("avgTitles", "avgCopies");
        avg_grid.setItems(this.statService.getAvgBookStats());


        Grid<CompanyStat> local_grid = new Grid<>(CompanyStat.class);
        local_grid.setColumns("city", "street", "bookTitles", "bookCopiesAvailable");
        local_grid.setMinHeight("80vh");
        local_grid.setItems(this.statService.getBookStatsPerStreet());

        vl.add(local_grid, avg_grid);
        dialog.add(vl);
        dialog.setWidth("1200px");
        dialog.setHeight("800px");
        dialog.open();
    }

    private void showAllInCity(ItemClickEvent<CompanyStat> e) {

        Dialog dialog = new Dialog();
        VerticalLayout vl = new VerticalLayout();
        vl.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);


        Grid<CompanyStat> local_grid = new Grid<>(CompanyStat.class);
        local_grid.setColumns("city", "street", "num_of_employees");
        local_grid.setMinHeight("40vh");
        local_grid.setItems(statService.getAllInCity(e.getItem().getCity()));

        vl.add(local_grid);
        dialog.add(vl);
        dialog.setWidth("800px");
        dialog.setHeight("200px");
        dialog.open();

    }

    private void showBranchDetail(ItemClickEvent<CompanyStat> e) {

        Dialog dialog = new Dialog();
        VerticalLayout vl = new VerticalLayout();
        vl.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        dialog.add(vl);

        TextField street = new TextField();
        street.setValue((e.getItem()).getStreet());
        street.setLabel("Street");
        street.setReadOnly(true);

        TextField city = new TextField();
        city.setValue((e.getItem()).getCity());
        city.setLabel("City");
        city.setReadOnly(true);

        TextField num = new TextField();
        num.setValue(""+e.getItem().getNum_of_employees());
        num.setLabel("Employee");
        num.setReadOnly(true);

        vl.add(city);
        vl.add(street);
        vl.add(num);
        dialog.setWidth("400px");
        dialog.setHeight("350px");
        dialog.open();
    }

    private void showOrderDetails(ItemClickEvent<Order> e){
        Dialog dialog = new Dialog();

        VerticalLayout vl = new VerticalLayout();
        Order order = e.getItem();

        Label header = new Label("Order " + order.getId());
        header.getStyle().set("font-size", "25px");
        vl.setAlignItems(FlexComponent.Alignment.CENTER);

        TextField sender = new TextField("Sender");
        sender.setValue(order.getStoreAddress());
        sender.setReadOnly(true);
        sender.setWidth("75%");
        TextField manufacturer = new TextField("Manufacturer");
        manufacturer.setReadOnly(true);
        manufacturer.setWidth("75%");
        manufacturer.setValue(order.getManufacturer());
        TextField manuMail = new TextField("Manufacturer\'s mail");
        manuMail.setReadOnly(true);
        manuMail.setWidth("75%");
        manuMail.setValue(order.getOrderItems().get(0).getManufacturer().getContact_mail());
        TextField manuPhone = new TextField("Manufacturer\'s phone");
        manuPhone.setValue(order.getOrderItems().get(0).getManufacturer().getContact_phone());
        manuPhone.setReadOnly(true);
        manuPhone.setWidth("75%");

        Hr hr = new Hr();
        hr.setWidth("75%");
        hr.getStyle().set("margin", "auto");
        vl.add(header, sender, manufacturer, manuMail, manuPhone, hr);

        List<List<Object>> prods = this.orderService.joinOrderItems(order);
        int overall = orderService.getOverAllN(prods);

        for(List<Object> pr: prods) {
            Product p = (Product) pr.get(0);

            TextField pid = new TextField("ID");
            pid.setValue(String.valueOf(p.getId()));
            pid.setReadOnly(true);
            pid.setWidth("75%");
            TextField n = new TextField("Number of ordered products");
            n.setReadOnly(true);
            n.setWidth("75%");
            n.setValue(String.valueOf((int)pr.get(1)));
            vl.add(pid, n);
            hr = new Hr();
            hr.setWidth("75%");
            hr.getStyle().set("margin", "auto");
            if(p instanceof Accessory) {
                Accessory temp = (Accessory) p;
                TextField name = new TextField("Accessory name");
                name.setValue(temp.getName());
                name.setReadOnly(true);
                name.setWidth("75%");
                TextField type = new TextField("Accessory type");
                type.setValue(temp.getType());
                type.setReadOnly(true);
                type.setWidth("75%");
                vl.add(name, type, hr);
            } else {
                BookCopy temp = (BookCopy) p;
                TextField name = new TextField("Book name");
                name.setValue(temp.getBook().getName());
                name.setReadOnly(true);
                name.setWidth("75%");
                TextField authorName = new TextField("Author\'s name");
                authorName.setValue(temp.getBook().getAuthor().getName());
                authorName.setReadOnly(true);
                authorName.setWidth("75%");
                TextField isbn = new TextField("ISBN");
                isbn.setValue(temp.getIsbn());
                isbn.setReadOnly(true);
                isbn.setWidth("75%");
                TextField date = new TextField("Date of publication");
                date.setValue(temp.getDayOfPublication().toString());
                date.setReadOnly(true);
                date.setWidth("75%");
                vl.add(name, authorName, isbn, date, hr);
            }
        }

        dialog.add(vl);
        dialog.setWidth("600px");
        dialog.setHeight("700px");
        dialog.open();
    }
}



