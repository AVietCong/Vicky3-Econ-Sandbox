package ui;

import model.*;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class SandboxGUI extends JFrame {

    private static final String JSON_STORE = "./data/Sandbox.json";
    private static final String ICON = "./data/Logo.png";
    private JsonReader jsonReader;
    private JsonWriter jsonWriter;
    private static final int BUTTON_PANEL_HEIGHT = 26;
    private static final Dimension INFORMATION_PANEL_DIMENSION = new Dimension(420, 100);
    private ImageIcon icon = new ImageIcon(ICON);
    private JButton saveButton;
    private JButton loadButton;
    private JButton marketButton;
    private JButton industryButton;
    private JButton constructionButton;
    private JButton nextTurnButton;
    private JLayeredPane interactionArea;
    private JPanel informationPanel;
    private String currentMenu;

    private java.util.List<Goods> allGoods;
    private java.util.List<Building> allBuildings;

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

    public SandboxGUI() throws FileNotFoundException {
        super("Economic Sandbox");
        jsonReader = new JsonReader(JSON_STORE);
        jsonWriter = new JsonWriter(JSON_STORE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setIconImage(icon.getImage());

        initEconomy();
        constructionSector.build(farm);
        constructionSector.build(chemicalPlant);
        constructionSector.build(ironMine);

        initializeGraphics();
        initializeInteraction();
        currentMenu = null;
    }



    private void initializeGraphics() {
        setSize(1280, 720);
        createButtons();
        initializeInteractionArea();
        informationPanel = new JPanel();
        interactionArea.add(informationPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    private void initializeInteractionArea() {
        interactionArea = new JLayeredPane();
        interactionArea.setLayout(new BorderLayout());
        add(interactionArea, BorderLayout.CENTER);
    }


    private void initializeInteraction() {
        initializeSaveLoadInteraction();
        initializeReportInteraction();
    }

    private void initializeSaveLoadInteraction() {
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveSandbox();
            }
        });

        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadSandbox();
            }
        });

        nextTurnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                processTurn();
            }
        });
    }

    private void initializeReportInteraction() {
        marketButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleMarketReport();
            }
        });

        industryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleIndustryReport();
            }
        });

        constructionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleConstructionReport();
            }
        });
    }

    private void handleConstructionReport() {
        interactionArea.remove(informationPanel);
        informationPanel = new JPanel();
        informationPanel.setLayout(new GridBagLayout());
        informationPanel.setBackground(Color.LIGHT_GRAY);
        informationPanel.setPreferredSize(new Dimension(200, 100));
        GridBagConstraints gbc = giveInfoPanelConstraints();

        addConstructionHeader(gbc, informationPanel);
        gbc.weighty = 0.1;
        gbc.gridy += 1;
        gbc.gridx = 0;
        addConstructionReport(gbc, informationPanel);
        gbc.weighty = 3.0;
        informationPanel.add(new JLabel(), gbc);
        refreshInteractionArea();
        currentMenu = "c";
    }

    private void addConstructionReport(GridBagConstraints gbc, JPanel informationPanel) {
        List<Building> queue = constructionSector.getConstructionQueue();
        List<Integer> value = constructionSector.getConstructionValue();
        for (Building building : queue) {
            informationPanel.add(new JLabel(building.getName()), gbc);
            gbc.gridy += 1;
        }
        gbc.gridx = 1;
        gbc.gridy = 1;
        for (Integer remaining : value) {
            informationPanel.add(new JLabel(String.valueOf(remaining)), gbc);
            gbc.gridy += 1;
        }
    }

    private void addConstructionHeader(GridBagConstraints gbc, JPanel informationPanel) {
        gbc.ipadx = 15;

        informationPanel.add(new JLabel("Building"), gbc);
        gbc.ipadx = 5;
        gbc.gridx += 1;
        informationPanel.add(new JLabel("Remaining"), gbc);
    }

    private void handleIndustryReport() {
        interactionArea.remove(informationPanel);
        informationPanel = new JPanel();
        informationPanel.setLayout(new GridBagLayout());
        informationPanel.setBackground(Color.LIGHT_GRAY);
        informationPanel.setPreferredSize(new Dimension(500, 100));
        GridBagConstraints gbc = giveInfoPanelConstraints();

        addIndustryReportHeader(gbc, informationPanel);
        gbc.gridx = 0;
        gbc.gridy += 1;
        gbc.weighty = 0.1;
        addRowsForActiveBuilding(gbc, informationPanel);
        addRowsForInactiveBuilding(gbc, informationPanel);

        gbc.weighty = 3.0;
        informationPanel.add(new JLabel(), gbc);
        refreshInteractionArea();
        currentMenu = "i";
    }

    private void addRowsForInactiveBuilding(GridBagConstraints gbc, JPanel informationPanel) {

    }

    private void addRowsForActiveBuilding(GridBagConstraints gbc, JPanel informationPanel) {

    }

    private void addIndustryReportHeader(GridBagConstraints gbc, JPanel informationPanel) {
        gbc.ipadx = 20;
        informationPanel.add(new JLabel("Factory"), gbc);
        gbc.ipadx = 5;
        gbc.gridx += 1;
        informationPanel.add(new JLabel("Income"), gbc);
        gbc.gridx += 1;
        informationPanel.add(new JLabel("Expense"), gbc);
        gbc.gridx += 1;
        informationPanel.add(new JLabel("Profit"), gbc);
        gbc.gridx += 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 2;
        informationPanel.add(new JLabel("Build/Remove"), gbc);
        gbc.gridwidth = 0;
    }

    private void handleMarketReport() {
        interactionArea.remove(informationPanel);
        informationPanel = new JPanel();
        informationPanel.setLayout(new GridBagLayout());
        informationPanel.setBackground(Color.LIGHT_GRAY);
        informationPanel.setPreferredSize(new Dimension(300, 100));
        GridBagConstraints gbc = giveInfoPanelConstraints();

        List<Goods> activeGoods = economy.getMarket().removeInactiveGoods().getAllGoods();
        addMarketReportHeader(gbc, informationPanel);
        gbc.weighty = 0.1;
        gbc.gridy += 1;
        gbc.gridx = 0;
        for (Goods goods : activeGoods) {
            addRowForGood(goods, gbc, informationPanel);
            gbc.gridy += 1;
            gbc.gridx = 0;
        }
        gbc.weighty = 3.0;
        informationPanel.add(new JLabel(), gbc);
        refreshInteractionArea();
        currentMenu = "m";
    }

    private void addMarketReportHeader(GridBagConstraints gbc, JPanel informationPanel) {
        gbc.ipadx = 5;

        informationPanel.add(new JLabel("Goods"), gbc);
        gbc.ipadx = 5;
        gbc.gridx += 1;
        informationPanel.add(new JLabel("Demand"), gbc);
        gbc.gridx += 1;
        informationPanel.add(new JLabel("Supply"), gbc);
        gbc.gridx += 1;
        informationPanel.add(new JLabel("Price"), gbc);
    }

    private void addRowForGood(Goods goods, GridBagConstraints gbc, JPanel informationPanel) {
        JLabel nameLabel = new JLabel(goods.getName());
        JLabel demandLabel = new JLabel(String.valueOf(goods.getDemand()));
        JLabel supplyLabel = new JLabel(String.valueOf(goods.getSupply()));
        JLabel priceLabel = new JLabel("£" + goods.determinePrice());

        gbc.ipadx = 5;
        informationPanel.add(nameLabel, gbc);
        gbc.gridx += 1;
        gbc.ipadx = 5;
        informationPanel.add(demandLabel, gbc);
        gbc.gridx += 1;
        informationPanel.add(supplyLabel, gbc);
        gbc.gridx += 1;
        informationPanel.add(priceLabel, gbc);
    }

    private void createButtons() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setPreferredSize(new Dimension(100, BUTTON_PANEL_HEIGHT));
        buttonPanel.setBackground(Color.LIGHT_GRAY);
        buttonPanel.setLayout(new FlowLayout(FlowLayout.LEADING, 0, 0));
        add(buttonPanel, BorderLayout.NORTH);

        saveButton = new JButton("Save");
        loadButton = new JButton("Load");
        marketButton = new JButton("Market");
        industryButton = new JButton("Industry");
        constructionButton = new JButton("Construction");
        nextTurnButton = new JButton("Next Turn");

        buttonPanel.add(saveButton);
        buttonPanel.add(loadButton);
        buttonPanel.add(marketButton);
        buttonPanel.add(industryButton);
        buttonPanel.add(constructionButton);
        buttonPanel.add(nextTurnButton);
    }

    private void refreshInteractionArea() {
        interactionArea.add(informationPanel, BorderLayout.WEST, Integer.valueOf(1));
        revalidate();
        repaint();
    }

    private GridBagConstraints giveInfoPanelConstraints() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weighty = 0.2;
        gbc.weightx = 0.2;
        gbc.gridx = 0;
        gbc.gridy = 0;
        return gbc;
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
    // EFFECTS: add all goods to the market
    private void setupMarket() {
        market = new Market();
        for (Goods goods : allGoods) {
            market.addGoods(goods);
        }
    }

    // MODIFIES: this
    // EFFECTS: process the economy in preparation for next turn
    private void processTurn() {
        constructAndDestroyBuildings();
        exchangeGoods();
        cleanupMarket();
        consumeAndProduce();
        if (currentMenu == "c") {
            handleConstructionReport();
        } else if (currentMenu == "m") {
            handleMarketReport();
        } else {
            handleIndustryReport();
        }
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
    }

    private void initEconomy() {
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
