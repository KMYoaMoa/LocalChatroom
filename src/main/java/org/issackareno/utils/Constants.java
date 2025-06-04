package org.issackareno.utils;

import java.text.SimpleDateFormat;

public class Constants {
    public static class Commands {
        public static final String EXIT = "command::exit";
        public static final String HELP = "command::help";
        public static final String PRINT_MEMBER = "command::print-member";
        public static final String PRINT_RECEIVER = "command::print-receiver";
    }
    public static class Texts {
        public static final String HELP_TEXT =
                "[System] Help & Command List:\n" +
                " * <command::exit> * \n" +
                " * - Close connection and exit.\n" +
                " * <command::help> * \n" +
                " * - Show command list.\n" +
                " * <command::print-member> * \n" +
                " * - List online users in the Chatroom.\n" +
                " * <command::print-receiver> * \n" +
                " * - List the users received your next message after being sent.\n" +
                " * * * * * * * * * * *";
    }
    public static String generateDate(long time) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        return dateFormat.format(time);
    }
}
