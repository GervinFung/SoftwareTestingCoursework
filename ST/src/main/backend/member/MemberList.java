package main.backend.member;

import main.backend.FileDataInvalidException;
import main.backend.fileinterface.MyFileMutator;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class MemberList implements MyFileMutator<Member> {

    private final static String MEMBER_FILE_NAME = "textFiles/member.txt";

    // create immutable list to reduce bug occurrence
    private final List<Member> memberList;

    public MemberList() { this.memberList = this.readFromFile(); }

    @Override
    public void add(final Member member) {
        this.memberList.add(member);
        this.writeToFile();
    }

    @Override
    public void remove(final Member member) {
        this.memberList.remove(member);
        this.writeToFile();
    }

    @Override
    public List<Member> getDataList() { return this.memberList; }

    @Override
    public List<Member> readFromFile() {

        final List<Member> memberList = new ArrayList<>();

        try (final BufferedReader reader = new BufferedReader(new FileReader(MEMBER_FILE_NAME))) {

            String line;
            while ((line = reader.readLine()) != null) {
                final String[] data = line.split("/");
                final int regNumber = Integer.parseInt(data[0]);
                final String name = data[1];
                final String password = data[2];
                final String address = data[3];
                final String contact = data[4];
                memberList.add(new Member(name, password, regNumber, new Address(address), contact));
            }

            reader.close();
            return memberList;

        } catch (final FileNotFoundException e) {
            throw new RuntimeException("File is missing: " + MEMBER_FILE_NAME);
        } catch (final IOException e) {
            throw new FileDataInvalidException("Error reading " + MEMBER_FILE_NAME);
        }
    }

    @Override
    public void writeToFile() {
        try (final BufferedWriter writer = new BufferedWriter(new FileWriter(MEMBER_FILE_NAME))) {

            for (final Member member : this.memberList) {
                writer.write(member.memberFormatToFile());
                writer.newLine();
            }

            writer.close();
            return;

        } catch (final IOException e) { e.printStackTrace(); }
        throw new FileDataInvalidException("Error reading " + MEMBER_FILE_NAME);
    }
}