package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class MainPage extends JFrame {
    private JTextField genreField;
    private JTextField ratingField;
    private JButton filterButton;
    private JTextArea recoText;
    public MainPage(JFrame loginFrame) {
        setTitle("Main Page");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Layout
        setLayout(new BorderLayout());

        // To add an image panel at the top
        JPanel imageJPanel = createImagePanel();
        add(imageJPanel, BorderLayout.NORTH);

        JPanel recommendations = createRecommendation();
        add(recommendations, BorderLayout.CENTER);

        JPanel controlPanel = createControlPanel();
        add(controlPanel, BorderLayout.SOUTH);

        setVisible(true);

        // Closing the Login Page
        if (loginFrame != null) {
            loginFrame.dispose();
        }
    }


    public void sortActors(List<Actor> actors) {
        Collections.sort(actors, new Comparator<Actor>() {
            @Override
            public int compare(Actor o1, Actor o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
    }

    private List<Actor> sortedActors() {
        List<Actor> copyActor = IMDB.getInstance().actors;
        IMDB.getInstance().sortActors();
        List<Actor> sortingActors = IMDB.getInstance().actors;

        // reinitialising actor list
        IMDB.getInstance().actors = copyActor;
        sortActors(sortingActors);
        return sortingActors;
    }

    private JPanel createImagePanel() {
        JPanel imagePanel = new JPanel();
        imagePanel.setLayout(new BoxLayout(imagePanel, BoxLayout.X_AXIS));
        ImageIcon imageIcon = new ImageIcon("src/main/resources/images/final2.png");
        JLabel imageLabel = new JLabel(imageIcon);
        imagePanel.add(imageLabel);
        return imagePanel;
    }

    private JPanel createControlPanel() {
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JButton viewActorsButton = new JButton("View Actors");
        JTextField searchingField = new JTextField(15);
        JLabel userLabel = new JLabel("User: " + LoginGUI.myUser.username + " | Experience: " + LoginGUI.myUser.experience);
        controlPanel.add(userLabel);

        JButton searchingButton = new JButton("Search movie/series/actor");
        JButton logoutButton = new JButton("Logout");
        JButton menuButton = new JButton("Menu");
        JButton notificationsButton = new JButton("View Notifications");

        viewActorsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                recoText.setText(sortedActors().toString());
            }
        });

        searchingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchText = searchingField.getText();
                List<String> results = searchingAlgorithm(searchText);
                recoText.setText(results.toString());
            }
        });

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new LoginGUI();
            }
        });

        menuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (LoginGUI.myUser.userType.equals(User.AccountType.Regular)) {
                    // Opening Regular Menu
                    new MenuRegular();

                } else if (LoginGUI.myUser.userType.equals(User.AccountType.Contributor)) {
                    // Opening Contributor Menu
                    new MenuContributor();

                } else if (LoginGUI.myUser.userType.equals(User.AccountType.Admin)) {
                    // Opening Admin Menu
                    new MenuAdmin();
                }
            }
        });

        notificationsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                recoText.setText(LoginGUI.myUser.notifications.toString());
            }
        });

        controlPanel.add(viewActorsButton);
        controlPanel.add(searchingField);
        controlPanel.add(searchingButton);
        controlPanel.add(logoutButton);
        controlPanel.add(notificationsButton);
        controlPanel.add(menuButton);

        return controlPanel;
    }

    private JPanel createRecommendation() {
        JPanel recoPanel = new JPanel();
        recoPanel.setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new FlowLayout());

        genreField = new JTextField(10);
        ratingField = new JTextField(10);
        filterButton = new JButton("Apply filters.");


        inputPanel.add(new JLabel("Genre:"));
        inputPanel.add(genreField);

        inputPanel.add(new JLabel("Nr. Ratings:"));
        inputPanel.add(ratingField);
        inputPanel.add(filterButton);

        recoText = new JTextArea(10, 30);
        recoText.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(recoText);
        recoPanel.add(inputPanel, BorderLayout.NORTH);
        recoPanel.add(scrollPane, BorderLayout.CENTER);

        initializeRecommendations();

        filterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                applyFilters();
            }
        });

        return recoPanel;
    }

    private void initializeRecommendations() {
        List<String> allProductions = IMDB.getInstance().productions.stream().map(Production::getTitle).collect(Collectors.toList());
        Collections.shuffle(allProductions);

        String recommendations = allProductions.stream().limit(5).collect(Collectors.joining("\n"));
        recoText.setText(recommendations);
    }

    private void applyFilters() {
        String genre = genreField.getText().trim();
        int nrRatings  = 0;
        try {
            nrRatings = Integer.parseInt(ratingField.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number");
            return;
        }

        List<Production> myProductions = filterProduction(genre, nrRatings);

        String recommendations = myProductions.stream().map(Production::toString).collect(Collectors.joining("\n"));
        recoText.setText(recommendations);
    }

    public List<Production> filterProduction(String genre, int nrRating) {
        // If there is no specific genre and any number of ratings
        List<Production> myProductions = new ArrayList<>();
        if (genre.equals("null")) {
            for (Production production : IMDB.getInstance().productions) {
                if (production.ratings.size() >= nrRating) {
                    myProductions.add(production);
                }
            }
        } else {
            // If it is a specific genre and any number of ratings
            for (Production production : IMDB.getInstance().productions) {
                for (Production.Genre myGenre : production.genres) {
                    if (myGenre.getName().contains(genre) && production.ratings.size() >= nrRating) {
                        myProductions.add(production);
                    }
                }
            }
        }
        return myProductions;
    }

    public List<String> searchingAlgorithm(String forSearch) {
        List<String> results =new ArrayList<>();
        for (Actor actor : IMDB.getInstance().actors) {
            if (actor.getName().equals(forSearch)) {
                results.add(actor.toString());
                break;
            }
        }

        for (Production production : IMDB.getInstance().productions) {
            if (production.getTitle().equals(forSearch)) {
                results.add(production.toString());
                break;
            }
        }
        return results;
    }
}
