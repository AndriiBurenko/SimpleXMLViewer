package dom;

import org.w3c.dom.Document;

import javax.swing.*;
import javax.xml.parsers.*;
import java.awt.event.*;
import java.io.*;

class DOMTreeFrame extends JFrame {
    private static final int DEFAULT_WIDTH = 400;
    private static final int DEFAULT_HEIGHT = 400;

    private DocumentBuilder builder;

    public DOMTreeFrame(){
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

        JMenu fileMenu = new JMenu("File");
        JMenuItem openItem = new JMenuItem("Open");
        openItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                openFile();
            }
        });
        fileMenu.add(openItem);

        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.exit(0);
            }
        });
        fileMenu.add(exitItem);

        JMenuBar menuBar = new JMenuBar();
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);
    }
    public void openFile(){
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File("dom"));

        chooser.setFileFilter(new javax.swing.filechooser.FileFilter(){

            @Override
            public boolean accept(File file) {
                return file.isDirectory() || file.getName().toLowerCase().endsWith(".xml");
            }

            @Override
            public String getDescription() {
                return "XML files";
            }
        });
        int r = chooser.showOpenDialog(this);
        if (r != JFileChooser.APPROVE_OPTION) return;
        final File file = chooser.getSelectedFile();

        new SwingWorker<Document, Void>(){
            protected Document doInBackground() throws Exception{
                if (builder == null){
                    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                    builder = factory.newDocumentBuilder();
                }
                return builder.parse(file);
            }
            protected void done(){
                try {
                    Document doc = get();
                    JTree tree = new JTree(new DOMTreeModel(doc));

                    setContentPane(new JScrollPane(tree));
                    validate();
                }catch (Exception e){
                    JOptionPane.showMessageDialog(DOMTreeFrame.this, e);
                }
            }
        }.execute();
    }
}
