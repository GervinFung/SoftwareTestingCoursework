package test.module.filehandler;

import main.backend.FileDataInvalidException;
import main.backend.fileinterface.MyFileMutator;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

public final class TestIOFile {

    @Rule
    public final TemporaryFolder tempFolder = new TemporaryFolder();

    //Test case 001, write String value into a file, read from it, compare to see if it's equal String value
    //this is to ensure that writer/reader is set up properly
    //so when problem arise, it's due to the arrangement of string in file

    @Test
    public void testWrite() throws IOException {
        // Create a temporary file.
        final File tempFile = tempFolder.newFile("tempFile.txt");


        final DummyTestMyFileMutator myFileMutator = new DummyTestMyFileMutator(tempFile);

        myFileMutator.add("a/");
        myFileMutator.add("b/");
        myFileMutator.add("c/");
        myFileMutator.add("d/");
        myFileMutator.add("e/");
        myFileMutator.remove("d/");


        // Write something to it.
        myFileMutator.writeToFile();

        // Read it from temp file
        final List<String> stringList = myFileMutator.readFromFile();
        final StringBuilder stringBuilder = new StringBuilder();
        stringList.forEach(stringBuilder::append);

        // Verify the content
        assertEquals(stringBuilder.toString(), "a/" + "b/" + "c/" + "e/");

        //File is guaranteed to be deleted after the test finishes.
    }


    //all class that implements said interface will use same read/write function
    private static final class DummyTestMyFileMutator implements MyFileMutator<String> {

        private final List<String> stringList;
        private final File tempFile;

        private DummyTestMyFileMutator(final File tempFile) {
            this.stringList = new ArrayList<>();
            this.tempFile = tempFile;
        }

        @Override
        public List<String> readFromFile() {
            final List<String> stringList = new ArrayList<>();

            try (final BufferedReader reader = new BufferedReader(new FileReader(tempFile))) {

                String line;
                while ((line = reader.readLine()) != null) {
                    stringList.add(line);
                }
                reader.close();
                //create unmodifiable list to improve immutability, reduce bug occurrence
                return Collections.unmodifiableList(stringList);

            } catch (final FileNotFoundException e) {
                throw new RuntimeException("File is missing: " + tempFile.getPath());
            } catch (final IOException e) {
                throw new FileDataInvalidException("Error reading " + tempFile.getPath());
            }
        }

        @Override
        public List<String> getDataList() { return this.stringList; }

        @Override
        public void writeToFile() {
            try (final BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

                for (final String string : this.stringList) {
                    writer.write(string);
                    writer.newLine();
                }

                writer.close();
                return;

            } catch (final IOException e) { e.printStackTrace(); }
            throw new FileDataInvalidException("Error reading " + tempFile.getPath());
        }

        @Override
        public void add(String s) { this.stringList.add(s); }

        @Override
        public void remove(String s) { this.stringList.remove(s); }
    }
}