package ui;

import model.*;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

// Economic Sandbox.json

public class SandboxArea {
    private static final String JSON_STORE = "./data/Sandbox.json";
    private Scanner input;
    private int turn;
    private JsonReader jsonReader;
    private JsonWriter jsonWriter;
    private List<Goods> allGoods;
    private List<Building> allBuildings;
    private List<Building> constructionReport;

    private Economy economy;
    private Industries industries;
    private Market market;
    private ConstructionSector constructionSector;

    private Goods clothes;
    private Goods fabric;
    private Goods furniture;
    private Goods grain;
    private Goods groceries;
    private Goods services;
    private Goods wood;
    private Goods coal;
    private Goods dye;
    private Goods explosives;
    private Goods fertilizer;
    private Goods glass;
    private Goods iron;
    private Goods lead;
    private Goods steel;
    private Goods sulfur;
    private Goods tools;

    private Building foodIndustry;
    private Building textileMill;
    private Building furnitureManufactory;
    private Building glassWork;
    private Building toolingWorkshop;
    private Building chemicalPlant;
    private Building steelMill;
    private Building farm;
    private Building ranch;
    private Building coalMine;
    private Building ironMine;
    private Building leadMine;
    private Building sulfurMine;
    private Building cottonPlantation;
    private Building dyePlantation;
    private Building urbanCenter;
    private Building loggingCamp;

    // run the sandbox area
    public SandboxArea() throws FileNotFoundException {
        input = new Scanner(System.in);
        jsonReader = new JsonReader(JSON_STORE);
        jsonWriter = new JsonWriter(JSON_STORE);
        runSandbox();
    }


    // MODIFIES: this
    // EFFECTS: handle user input in main menu
    private void runSandbox() {
        boolean keepGoing = true;
        String command = null;
        String exitCommand = null;

        turn = 1;
        init();

        while (keepGoing) {
            displayMenu();
            command = input.next();
            command = command.toLowerCase();

            if (command.equals("exit")) {
                displayExitMenu();
                exitCommand = input.next();
                exitCommand = exitCommand.toLowerCase();
                if (exitCommand.equals("yes")) {
                    saveSandbox();
                }
                keepGoing = false;
            } else {
                processCommand(command);
            }
        }

        System.out.println("\n Exiting Sandbox");
    }

    // EFFECTS: display exit menu options
    private void displayExitMenu() {
        System.out.println("\n Do you want to save file before exiting? (Yes/No)");
    }

    // EFFECTS: display main menu options
    private void displayMenu() {
        if (turn == 1) {
            System.out.println("\n Welcome to the economic sandbox!");
        }
        System.out.println("\n Week " + turn);
        for (Building b : constructionReport) {
            System.out.println("\t" + b.getName() + " has been constructed!");
        }
        System.out.println("\t market -> View Market Report");
        System.out.println("\t industry -> View Industry Report");
        System.out.println("\t construction -> View/Add/Remove Buildings from Construction Queue");
        System.out.println("\t down size -> Reduce a Building's Level");
        System.out.println("\t next turn -> Fast Forward 1 Week");
        System.out.println("\t load -> Load Sandbox to File");
        System.out.println("\t exit -> Exit Sandbox");
    }

    // MODIFIES: this
    // EFFECTS: process user input
    private void processCommand(String command) {
        if (command.equals("market")) {
            printMarketReport();
        } else if (command.equals("industry")) {
            printIndustryReport();
        } else if (command.equals("construction")) {
            handleConstructionMenu();
        } else if (command.equals("down size")) {
            handleDownSize();
        } else if (command.equals("next turn")) {
            processTurn();
        } else if (command.equals("load")) {
            loadSandbox();
        } else {
            System.out.println("Selection not valid");
        }
    }

    // EFFECTS: save sandbox to file
    private void saveSandbox() {
        try {
            jsonWriter.open();
            jsonWriter.write(economy);
            jsonWriter.close();
            System.out.println("Saved current state of economy to " + JSON_STORE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        }
    }

    // MODIFIES: this
    // EFFECTS: loads economy from file
    private void loadSandbox() {
        try {
            economy = jsonReader.read();
            System.out.println("Loaded last state of economy from " + JSON_STORE);
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        }
    }

    // MODIFIES: this
    // EFFECTS: handle user input in downsize menu
    private void handleDownSize() {
        boolean finished = false;
        String command = null;
        int choice;

        while (!finished) {
            displayDownsizeOptions();
            command = input.next();
            command = command.toLowerCase();

            if (command.equals("back")) {
                finished = true;
            } else {
                choice = Integer.parseInt(command);
                processDownSizeCommand(choice);
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: process downsize action from user
    private void processDownSizeCommand(int choice) {
        int levels;
        if (choice >= economy.getIndustries().getAllBuildings().size() || choice < 0) {
            System.out.println("Selection not valid");
        } else {
            System.out.println("\n Choose number of levels to downsize");
            levels = input.nextInt();
            if (economy.getIndustries().getAllBuildings().get(choice).downsize(levels)) {
                System.out.println("Downsize successful");
            } else {
                System.out.println("Can't downsize lower than 0");
            }
        }
    }

    // EFFECTS: display all buildings that can be downsized
    private void displayDownsizeOptions() {
        for (int i = 0; i < economy.getIndustries().getAllBuildingNames().size(); i++) {
            System.out.println(i + "\t" + economy.getIndustries().getAllBuildingNames().get(i));
        }
        System.out.println("\n Choose Building to Remove");
        System.out.println("\t back -> back to Construction Menu");
    }


    // MODIFIES: this
    // EFFECTS: process the economy in preparation for next turn
    private void processTurn() {
        constructAndDestroyBuildings();
        exchangeGoods();
        cleanupMarket();
        consumeAndProduce();
        turn += 1;
    }

    // MODIFIES: this
    // EFFECTS: change the demand and supply of goods in market
    private void consumeAndProduce() {
        for (Building b : economy.getIndustries().getAllBuildings()) {
            b.consume();
            b.produce();
        }
    }

    // MODIFIES: this
    // EFFECTS: reset demand and supply for all but consumer goods; consumer goods gain 5 demand
    private void cleanupMarket() {
        for (Goods goods : economy.getMarket().returnIndustrialGoods()) {
            goods.setDemand(0);
            goods.setSupply(0);
        }
        for (Goods goods : economy.getMarket().returnConsumerGoods()) {
            goods.setSupply(0);
            goods.addDemand(5);
        }
    }

    // MODIFIES: this
    // EFFECTS: set income and expenses of buildings
    private void exchangeGoods() {
        for (Building b : economy.getIndustries().getAllBuildings()) {
            b.payExpense();
            b.gainIncome();
        }
    }

    // MODIFIES: this
    // EFFECTS: construct new buildings remove buildings with size 0 from industry
    private void constructAndDestroyBuildings() {
        if (!economy.getConstructionSector().getConstructionQueue().isEmpty()) {
            economy.getConstructionSector().consume();
            economy.getConstructionSector().payExpense();
        }
        List<Building> constructedBuilding = economy.getConstructionSector().construct();
        for (Building b : constructedBuilding) {
            if (economy.getIndustries().getAllBuildings().contains(b)) {
                b.expand();
            } else {
                economy.getIndustries().add(b);
            }
        }
        constructionReport = constructedBuilding;
    }

    // MODIFIES: this
    // EFFECTS: handle user input in the construction menu
    private void handleConstructionMenu() {
        boolean finished = false;
        String command = null;

        while (!finished) {
            displayConstructionMenu();
            command = input.next();
            command = command.toLowerCase();

            if (command.equals("back")) {
                finished = true;
            } else {
                processConstructionCommand(command);
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: process user command in construction menu
    private void processConstructionCommand(String command) {
        if (command.equals("build")) {
            handleBuild();
        } else if (command.equals("remove")) {
            handleRemove();
        } else {
            System.out.println("Selection not valid");
        }
    }

    // MODIFIES: this
    // EFFECTS: handle user command in Remove menu
    private void handleRemove() {
        boolean finished = false;
        String command = null;
        int choice;

        while (!finished) {
            displayRemoveOptions();
            command = input.next();

            if (command.equals("back")) {
                finished = true;
            } else {
                choice = Integer.parseInt(command);
                doRemove(choice);
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: process user command in Remove menu
    private void doRemove(int index) {
        if (index >= economy.getIndustries().getAllBuildings().size() || index < 0) {
            System.out.println("Selection is not valid");
        } else {
            if (economy.getConstructionSector().remove(industries.getAllBuildings().get(index))) {
                System.out.println("Remove Successfully");
            } else {
                System.out.println("Building is not in Queue");
            }
        }
    }

    // EFFECTS: display all options to remove from construction queue
    private void displayRemoveOptions() {
        for (int i = 0; i < economy.getIndustries().getAllBuildings().size(); i++) {
            System.out.println(i + "\t" + economy.getIndustries().getAllBuildingNames().get(i));
        }
        System.out.println("\n Choose Building to Remove");
        System.out.println("\t back -> back to Construction Menu");
    }

    // MODIFIES: this
    // EFFECTS: handle user command in the Build menu
    private void handleBuild() {
        boolean finished = false;
        String command = null;
        int choice;

        while (!finished) {
            displayConstructionOptions();
            command = input.next();

            if (command.equals("back")) {
                finished = true;
            } else {
                choice = Integer.parseInt(command);
                doBuild(choice);
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: process user command in the Build menu
    private void doBuild(int index) {
        if (index >= economy.getIndustries().getAllBuildings().size() || index < 0) {
            System.out.println("Selection is not valid");
        } else {
            economy.getConstructionSector().build(economy.getIndustries().getAllBuildings().get(index));
        }
    }

    // EFFECTS: display all options that can be built
    private void displayConstructionOptions() {
        for (int i = 0; i < economy.getIndustries().getAllBuildings().size(); i++) {
            System.out.println(i + "\t" + economy.getIndustries().getAllBuildings().get(i).getName());
        }
        System.out.println("\n Choose Building to Construct");
        System.out.println("\t back -> back to Construction Menu");
    }

    // EFFECTS: display construction queue and further options in Construction menu
    private void displayConstructionMenu() {
        List<Building> constructionQueue = economy.getConstructionSector().getConstructionQueue();
        List<Integer> progressQueue = economy.getConstructionSector().getConstructionValue();

        System.out.println("Name\t Remaining Progress");
        for (int i = 0; i < constructionQueue.size(); i++) {
            System.out.println(constructionQueue.get(i).getName() + "\t" + progressQueue.get(i));
        }

        System.out.println("\t build -> build or expand a building");
        System.out.println("\t remove -> remove building from queue");
        System.out.println("\t back -> back to main menu");
    }

    // EFFECTS: display a list of industries with its expenses and profits
    private void printIndustryReport() {
        Industries activeBuildings = economy.getIndustries().removeEmptyBuildings();
        List<String> names = activeBuildings.getAllBuildingNames();
        List<Integer> size = activeBuildings.getAllSize();
        List<Integer> expenses = activeBuildings.getAllExpenses();
        List<Integer> income = activeBuildings.getAllIncome();

        String industryReportTitleTemplate = "%-20s %3s %10s %10s%n";
        String industryReportTemplate = "%-20s %3s %10s %10s%n";
        System.out.printf(industryReportTitleTemplate, "Industry", "Size", "Expense", "Income");
        for (int i = 0; i < names.size(); i++) {
            System.out.printf(industryReportTemplate, names.get(i), size.get(i), expenses.get(i), income.get(i));
        }
    }

    // EFFECTS: display a list of Goods with its supply, demand and price modifier
    private void printMarketReport() {
        Market activeGoods = economy.getMarket().removeInactiveGoods();
        List<String> names = activeGoods.getAvailableGoods();
        List<Integer> demand = activeGoods.getDemandOfGoods();
        List<Integer> supply = activeGoods.getSupplyOfGoods();
        List<Double> priceModifiers = activeGoods.getPriceModifiers();
        List<Integer> prices = activeGoods.getPrices();

        String marketReportTitleTemplate = "%-10s %7s %7s %6s %7s%n";
        String marketReportTemplate = "%-10s %7s %7s %8s %12s%n";
        System.out.printf(marketReportTitleTemplate, "Goods", "Demand", "Supply",
                "Price Modifier", "Price");
        for (int i = 0; i < names.size(); i++) {
            int modifier = (int) ((priceModifiers.get(i) - 1) * 100);
            String modifierString = modifier + "%";
            String priceString = "£" + prices.get(i);
            System.out.printf(marketReportTemplate, names.get(i), demand.get(i), supply.get(i),
                    modifierString, priceString);
        }
    }

    // Set up starting situation to that of Sweden in Victoria 3
    // MODIFIES: this
    // EFFECTS: initializes starting conditions
    private void init() {
        initGoods();
        allGoods = Arrays.asList(clothes, fabric, furniture, grain, groceries, services, wood, coal, dye,
                explosives, fertilizer, glass, iron, lead, steel, sulfur, tools);
        initIndustryBuildings();
        initAgricultureBuildings();
        initOtherBuildings();
        allBuildings = Arrays.asList(foodIndustry, textileMill, furnitureManufactory, glassWork, toolingWorkshop,
                chemicalPlant, steelMill, urbanCenter, farm, ranch, cottonPlantation, dyePlantation, coalMine, ironMine,
                leadMine, sulfurMine);
        constructionSector = new ConstructionSector(Arrays.asList(steel, tools, explosives), Arrays.asList(70, 20, 20));
        setupIndustry();
        setupMarket();
        setDemandForConsumerGoods();
        for (Building b : industries.getAllBuildings()) {
            b.consume();
            b.produce();
            b.payExpense();
            b.gainIncome();
        }
        economy = new Economy(industries, market, constructionSector);
        input = new Scanner(System.in);
        input.useDelimiter("\n");
        constructionReport = new ArrayList<>();
    }

    // MODIFIES: this
    // EFFECTS: add all goods to the market
    private void setupMarket() {
        market = new Market();
        for (Goods goods : allGoods) {
            market.addGoods(goods);
        }
    }

    // MODIFIES: this
    // EFFECTS: set demand of consumer goods
    private void setDemandForConsumerGoods() {
        clothes.setDemand(410);
        furniture.setDemand(289);
        grain.setDemand(313);
        services.setDemand(425);
    }

    // MODIFIES: this
    // EFFECTS: initializes all goods available for the sandbox
    private void initGoods() {
        clothes = new Goods("Clothes", 30, Goods.GoodsType.CONSUMER);
        fabric = new Goods("Fabric", 20, Goods.GoodsType.INDUSTRIAL);
        furniture = new Goods("Furniture", 30, Goods.GoodsType.CONSUMER);
        grain = new Goods("Grain", 20, Goods.GoodsType.INDUSTRIAL);
        groceries = new Goods("Groceries", 30, Goods.GoodsType.CONSUMER);
        services = new Goods("Services", 30, Goods.GoodsType.CONSUMER);
        wood = new Goods("Wood", 20, Goods.GoodsType.INDUSTRIAL);
        coal = new Goods("Coal", 30, Goods.GoodsType.INDUSTRIAL);
        dye = new Goods("Dye", 40, Goods.GoodsType.INDUSTRIAL);
        explosives = new Goods("Explosives", 50, Goods.GoodsType.INDUSTRIAL);
        fertilizer = new Goods("Fertilizer", 30, Goods.GoodsType.INDUSTRIAL);
        glass = new Goods("Glass", 40, Goods.GoodsType.INDUSTRIAL);
        iron = new Goods("Iron", 40, Goods.GoodsType.INDUSTRIAL);
        lead = new Goods("Lead", 40, Goods.GoodsType.INDUSTRIAL);
        steel = new Goods("Steel", 50, Goods.GoodsType.INDUSTRIAL);
        sulfur = new Goods("Sulfur", 50, Goods.GoodsType.INDUSTRIAL);
        tools = new Goods("Tools", 40, Goods.GoodsType.INDUSTRIAL);
    }

    // MODIFIES: this
    // EFFECTS: initializes all agricultural buildings
    private void initAgricultureBuildings() {
        farm = new Building("Farm", 150,
                Arrays.asList(fertilizer), Arrays.asList(40),
                Arrays.asList(grain), Arrays.asList(150));
        ranch = new Building("Livestock Ranch", 150,
                Arrays.asList(grain), Arrays.asList(10),
                Arrays.asList(fabric, fertilizer), Arrays.asList(40, 10));
        cottonPlantation = new Building("Cotton Plantation", 150,
                Arrays.asList(tools), Arrays.asList(5),
                Arrays.asList(fabric), Arrays.asList(40));
        dyePlantation = new Building("Dye Plantation", 150,
                Arrays.asList(tools), Arrays.asList(5),
                Arrays.asList(dye), Arrays.asList(25));
    }

    // MODIFIES: this
    // EFFECTS: initializes all industrial buildings
    private void initIndustryBuildings() {
        foodIndustry = new Building("Food Industry", 450,
                Arrays.asList(grain, iron), Arrays.asList(50, 20),
                Arrays.asList(groceries), Arrays.asList(65));
        textileMill = new Building("Textile Mill", 450,
                Arrays.asList(fabric, dye, tools), Arrays.asList(60, 10, 5),
                Arrays.asList(clothes), Arrays.asList(100));
        furnitureManufactory = new Building("Furniture Manufactory", 450,
                Arrays.asList(wood, fabric, tools), Arrays.asList(50, 10, 10),
                Arrays.asList(furniture), Arrays.asList(110));
        glassWork = new Building("Glass Work", 450,
                Arrays.asList(lead), Arrays.asList(35),
                Arrays.asList(glass), Arrays.asList(70));
        toolingWorkshop = new Building("Tooling Workshop", 450,
                Arrays.asList(wood, steel), Arrays.asList(30, 20),
                Arrays.asList(tools), Arrays.asList(80));
        chemicalPlant = new Building("Chemical Plant", 450,
                Arrays.asList(sulfur, iron, coal), Arrays.asList(60, 30, 15),
                Arrays.asList(fertilizer, explosives), Arrays.asList(110, 70));
        steelMill = new Building("Steel Mill", 450,
                Arrays.asList(iron, coal), Arrays.asList(90, 30),
                Arrays.asList(steel), Arrays.asList(120));
    }

    // MODIFIES: this
    // EFFECTS: initializes remaining buildings
    private void initOtherBuildings() {
        coalMine = new Building("Coal Mine", 300,
                Arrays.asList(tools, explosives), Arrays.asList(15, 10),
                Arrays.asList(coal), Arrays.asList(85));
        ironMine = new Building("Iron Mine", 300,
                Arrays.asList(tools, coal, explosives), Arrays.asList(15, 15, 10),
                Arrays.asList(iron), Arrays.asList(80));
        leadMine = new Building("Lead Mine", 300,
                Arrays.asList(tools, coal, explosives), Arrays.asList(15, 15, 10),
                Arrays.asList(coal), Arrays.asList(80));
        sulfurMine = new Building("Sulfur Mine", 300,
                Arrays.asList(tools, coal, explosives), Arrays.asList(15, 15, 10),
                Arrays.asList(coal), Arrays.asList(80));
        loggingCamp = new Building("Logging Camp", 150,
                Arrays.asList(tools), Arrays.asList(5),
                Arrays.asList(wood), Arrays.asList(60));
        urbanCenter = new Building("Urban Center", 450,
                Arrays.asList(glass, steel, coal), Arrays.asList(10, 10, 10),
                Arrays.asList(services), Arrays.asList(110));
    }

    // MODIFIES: this
    // EFFECTS: set up the starting industries
    private void setupIndustry() {
        List<Building> startingIndustries = Arrays.asList(textileMill, toolingWorkshop, urbanCenter, farm,
                ranch, ironMine, loggingCamp);
        industries = new Industries();
        for (Building b : allBuildings) {
            industries.add(b);
            if (!startingIndustries.contains(b)) {
                b.downsize(1);
            }
        }
        textileMill.setSize(3);
        urbanCenter.setSize(8);
        farm.setSize(11);
        ranch.setSize(5);
        ironMine.setSize(7);
        loggingCamp.setSize(16);
    }
}
