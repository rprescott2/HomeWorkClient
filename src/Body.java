
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.nio.ByteBuffer;

import static javax.swing.JFrame.EXIT_ON_CLOSE;

public class Body {
    String ip;
    private static Socket clientSocket;
    private static BufferedReader reader;
    private static BufferedReader in;
    private static BufferedWriter out;
    int FailNumber = 0;
    String UserName = System.getProperty("user.name");
    JTextField adminLogin = new JTextField(null);
    JTextField adminPassword = new JTextField(null);
    JTextField key = new JTextField();
    JLabel KeyEntery = new JLabel("Введите ключ:");
    JFrame MainFrame = new JFrame("Домашнее задание");
    JPanel MainPanel = new JPanel( );
    JButton okey = new JButton("Окей");
    JButton back = new JButton("Назад");
    private void ButtonBack(ActionEvent e){
        key.setText(null);
        MainFrame.setContentPane(MainPanel);
        MainFrame.validate();
        key.requestFocusInWindow();
        try {
            out.write("Back" + "\n");
            out.flush();
            in.close();
            out.close();
            reader.close();
            clientSocket.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
    class DrawPane extends JPanel {
        @Override
        public void paint(Graphics g) {
            try {
                InputStream inputStream = clientSocket.getInputStream();
                byte[] sizeAr = new byte[4];
                inputStream.read(sizeAr);
                int size = ByteBuffer.wrap(sizeAr).asIntBuffer().get();

                byte[] imageAr = new byte[size];
                inputStream.read(imageAr);
                BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageAr));
                g.drawImage(image, 0, 0,this);
            } catch (IOException e) {
            }
        }
    }
    JButton info = new JButton("Инфо");

    private void ButtonOkey(ActionEvent e) {
        String read = null;
        try {
            clientSocket = new Socket(ip, 9999);
            reader = new BufferedReader(new InputStreamReader(System.in));
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            String okeyString = "okey";
            out.write(okeyString + "\n");
            out.flush();
            okey.setEnabled(false);
            String key2 = key.getText();
            out.write(key2 + "\n");
            out.flush();
            read = in.readLine();
        } catch (IOException el){
        }
        try {
            if (read.equals("true")) {
                FailNumber = 0;
                key.setText(null);
                JFrame homework = new JFrame();
                homework.setSize(1000,1000);
                homework.setContentPane(new DrawPane());
                homework.setVisible(true);
                homework.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                homework.addKeyListener(new KeyListener() {
                    @Override
                    public void keyTyped(KeyEvent e) {
                    }

                    @Override
                    public void keyPressed(KeyEvent e) {
                        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                            homework.dispose();
                            try {
                                clientSocket.close();
                                in.close();
                                out.close();
                                reader.close();
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void keyReleased(KeyEvent e) {
                    }
                });
                homework.setSize(400, 500);
            } else {
                FailNumber++;
                JLabel fails = new JLabel();
                JPanel Fail = new JPanel();
                Fail.add(fails);
                Fail.add(back);
                MainFrame.setContentPane(Fail);
                if ((3 - FailNumber) == 0) {
                    fails.setText("Доступ закрыт");
                    back.removeAll();
                    back.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            MainFrame.dispose();

                        }
                    });
                } else {
                    fails.setText("У вас осталось " + (3 - FailNumber) + " попытки");
                    MainFrame.validate();
                    key.setText(null);
                }
            }
        } catch (NullPointerException el){
            JPanel serverstop = new JPanel();
                JLabel serverstips = new JLabel("Сервер отключен");
                serverstop.add(serverstips);
                JButton back4 = new JButton("Назад");
                serverstop.add(back4);
                back4.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        key.setText(null);
                        key.requestFocusInWindow();
                        MainFrame.setContentPane(MainPanel);
                    }
                });
                MainFrame.setContentPane(serverstop);
                MainFrame.validate();

        }
    }
    JButton Admin = new JButton("Админ");
    public void ButtonAdmin(ActionEvent e){
        try {
            clientSocket = new Socket(ip, 9999);
        } catch(IOException el){
            el.printStackTrace();
        }

        JLabel adminlog = new JLabel("Логин:");
        JLabel adminpass = new JLabel("Пароль:");
        JPanel admin = new JPanel();
        JButton okey2 = new JButton("Окей");

        GridBagLayout grig = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        admin.setLayout(grig);
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.FIRST_LINE_END;
        gbc.insets = new Insets(10,0,10,10);
        gbc.gridx = 100;
        gbc.weightx = 100.0;
        gbc.weighty = 0.0;
        gbc.gridy = 10;
        admin.add(back,gbc);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0,0,0,0);
        gbc.gridx = 40;
        gbc.gridy = 25;
        gbc.gridwidth = 5;
        gbc.gridheight = 5;
        admin.add(adminlog, gbc);
        gbc.gridx = 40;
        gbc.gridy = 30;
        gbc.gridheight = 1;
        admin.add(adminpass, gbc);
        gbc.gridx = 45;
        gbc.gridy = 25;
        gbc.gridwidth = 5;
        gbc.ipadx = 200;
        gbc.gridheight = 5;
        admin.add(adminLogin,gbc);
        gbc.gridx = 45;
        gbc.gridy = 30;
        gbc.gridwidth = 5;
        gbc.ipadx = 200;
        gbc.gridheight = 1;
        admin.add(adminPassword, gbc);
        gbc.ipadx = 0;
        gbc.gridx = 48;
        gbc.gridy = 33;
        gbc.gridwidth = 5;
        admin.add(okey2,gbc);
        JLabel label = new JLabel();
        gbc.weightx = 0.0;
        gbc.weighty = 100.0;
        gbc.gridy = 100;
        gbc.gridx = 10;
        gbc.anchor = GridBagConstraints.LAST_LINE_START;
        admin.add(label,gbc);
        adminLogin.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                okey.setEnabled(true);
            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    back.doClick();
                }else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    adminPassword.requestFocusInWindow();
                }
            }
        });
        adminPassword.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                okey.setEnabled(true);
            }

            @Override
            public void keyPressed(KeyEvent e) {

                }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    back.doClick();
                }else if (e.getKeyCode() == KeyEvent.VK_UP) {
                    adminLogin.requestFocusInWindow();
                }
            }
        });

        okey2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String read = null;
                try {
                    reader = new BufferedReader(new InputStreamReader(System.in));
                    in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                    out.write("Admin" + "\n");
                    out.flush();
                    out.write(adminLogin.getText() + "\n");
                    out.flush();
                    out.write(adminPassword.getText() + "\n");
                    out.flush();
                    read = in.readLine();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                if(read.contentEquals("true")) {
                    adminLogin.setText(null);
                    adminPassword.setText(null);
                    JPanel entry = new JPanel();
                    JLabel keytwo = new JLabel("Ключ");
                    JTextField key1 = new JTextField(null);
                    entry.setLayout(null);
                    entry.add(key1);
                    entry.add(keytwo);
                    keytwo.setBounds(60,190,80,24);
                    key1.setBounds(92,190,270,24);
                    JButton addd = new JButton("Добавить");
                    addd.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            try {
                                out.write("Add" + "\n");
                                out.flush();
                                out.write(key1.getText() + "\n");
                                out.flush();
                                key1.setText(null);
                                MainFrame.validate();
                            }catch (IOException e1){
                            }
                        }
                    });
                    entry.add(addd);
                    addd.setBounds(260,220,100,24);
                    entry.add(back);
                    back.setBounds(10,15,80,25);
                    key1.addKeyListener(new KeyListener() {
                        @Override
                        public void keyTyped(KeyEvent e) {

                        }

                        @Override
                        public void keyPressed(KeyEvent e) {
                            if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
                                back.doClick();
                            } else if(e.getKeyCode() == KeyEvent.VK_ENTER & !key1.getText().equals(null)){
                                addd.doClick();
                            }
                        }

                        @Override
                        public void keyReleased(KeyEvent e) {

                        }
                    });
                    MainFrame.setContentPane(entry);
                    MainFrame.validate();
                    key1.requestFocusInWindow();
                } else {
                    back.doClick();
                    adminLogin.setText(null);
                    adminPassword.setText(null);
                }
            }
        });
        MainFrame.setContentPane(admin);
        MainFrame.validate();
        adminLogin.requestFocusInWindow();
    }
    public Body() throws Exception {
        ip = new GetIpAdress().run2().replaceFirst("/","");
       okey.setEnabled(false);
       MainFrame.setContentPane(MainPanel);
       GridBagLayout gridBagLayout = new GridBagLayout();
       MainFrame.setLayout(gridBagLayout);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.NONE;
        gbc.ipady = 5;
        gbc.ipadx = 15;
        gbc.weighty = 1.0;
        gbc.weightx = 100.0;
        gbc.anchor = GridBagConstraints.LAST_LINE_END;
        gbc.insets = new Insets(10,0,5,5);
        gbc.gridx = 100;
        gbc.gridwidth = 2;
        gbc.gridy = 1;
        MainFrame.add(Admin,gbc);
        MainFrame.add(okey, gbc);
        gbc.gridx = 49;
        gbc.ipady = 5;
        gbc.ipadx = 15;
        gbc.weightx = 100.0;
        gbc.gridwidth = 1 ;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10,10,0,0);
        MainFrame.add(KeyEntery, gbc);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weightx = 0.0;
        gbc.gridwidth = 5;
        gbc.gridx = 50;
        gbc.ipadx = 200;
        gbc.insets = new Insets(10,0,0,0);
        MainFrame.add(key,gbc);
        gbc.gridx = 5;
        gbc.ipadx = 15;
        gbc.gridx = 100;
        gbc.insets = new Insets(10,0,5,5);
        gbc.weightx = 100.0;
        gbc.gridwidth = 5;
        gbc.anchor = GridBagConstraints.FIRST_LINE_END;
        MainFrame.add(info, gbc);
        MainFrame.setVisible(true);
            MainFrame.setSize(400, 500);
            key.addKeyListener(new KeyListener() {
                @Override
                public void keyTyped(KeyEvent e) {
                    okey.setEnabled(true);
                }

                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER & !key.getText().equals(null)) {
                        okey.doClick();
                    } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                        MainFrame.dispose();
                    }
                }

                @Override
                public void keyReleased(KeyEvent e) {

                }
            });
            info.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JPanel infoPanel = new JPanel();
                    JLabel infolabel = new JLabel("Это мое приложение");
                    infolabel.setLayout(new FlowLayout());
                    infoPanel.add(infolabel);
                    JButton back3 = new JButton("Назад");
                    infoPanel.add(back3);
                    back3.setBounds(200,200,100,100);
                    back3.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            MainFrame.setContentPane(MainPanel);
                            MainFrame.validate();
                        }
                    });
                    infolabel.requestFocusInWindow();
                    infolabel.addKeyListener(new KeyListener() {
                        @Override
                        public void keyTyped(KeyEvent e) {

                        }

                        @Override
                        public void keyPressed(KeyEvent e) {
                            if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
                                back3.doClick();
                                key.requestFocusInWindow();
                            }
                        }

                        @Override
                        public void keyReleased(KeyEvent e) {

                        }
                    });

                    infolabel.setBounds(100,100,100,100);
                    MainFrame.setContentPane(infoPanel);
                    MainFrame.validate();
                }
            });
            Admin.setVisible(false);
            key.addKeyListener(new KeyListener() {
                @Override
                public void keyTyped(KeyEvent e) {

                }

                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_UP) {
                        Admin.doClick();
                    }
                }

                @Override
                public void keyReleased(KeyEvent e) {

                }
            });

            MainFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);
            Admin.addActionListener(this::ButtonAdmin);
            okey.addActionListener(this::ButtonOkey);
            MainFrame.setContentPane(MainPanel);
            back.addActionListener(this::ButtonBack);
            key.requestFocusInWindow();
    }
}