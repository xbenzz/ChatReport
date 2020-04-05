package me.mreport.Utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import me.mreport.ChatR;
import net.md_5.bungee.api.chat.TextComponent;

public class Utils {
	  
	  public static String reportDenied(String name) {
		  String d = "&c&m------------------------------------------------";
		  String d2 = "&c  Your report has been denied by a staff member.";
		  String d3 = "&c  This player was not committing any chat offences.";
		  String d4 = "&c  Reported Player: &6" + name;
		  String d5 = " ";
		  String d6 = "&f  View all punishable offenses at &e" + ChatR.getInstance().getConfig().getString("Rules-Link");
		  String d7 = "&c&m------------------------------------------------";
		  return d + "\n" + d2 + "\n" + d3 + "\n" + d4 + "\n" + d5 + "\n" + d6 + "\n" + d7;
	  }
	  
	  public static String reportAccepted(String name) {
		  String a = "&c&m------------------------------------------------";
		  String a2 = "&6  Your report has been accepted by a staff member!";
		  String a3 = "&6  This player has now been punished accordingly!";
		  String a4 = "&6  Reported Player: &a&n" + name;
		  String a5 = "&c&m------------------------------------------------";
		  return a + "\n" + a2 + "\n" + a3 + "\n" + a4 + "\n" + a5;
	  }
	  
	  public static String reportSent(String name) {
		  String s = "&c&m------------------------------------------------";
		  String s2 = "&6  Your report has been submitted!";
		  String s3 = "&6  Reported Player: &a&n" + name;
		  String s4 = "&r ";
		  String s5 = "&e  The past 10 minutes of public chat history for this";
		  String s6 = "&e  player has been saved for staff members to look at!";
		  String s7 = "&c&m------------------------------------------------";
		  return s + "\n" + s2 + "\n" + s3 + "\n" + s4 + "\n" + s5 + "\n" + s6 + "\n" + s7;
	  }
	  
	  public static String pastTen() {
		  String u = "&c&m------------------------------------------------";
		  String u2 = "&c  Unable to report player! This player has already been";
		  String u3 = "&c  reported in the past 10 minutes!";
		  String u4 = "&c&m------------------------------------------------";
		  return u + "\n" + u2 + "\n" + u3 + "\n" + u4;
	  }
	 
	  public static String notType() {
		  String n = "&c&m------------------------------------------------";
		  String n2 = "&c  This player has not typed any messages recently!";
		  String n3 = "&c&m------------------------------------------------";  
		  return n + "\n" + n2 + "\n" + n3;
	  }
	  
	  public static String cooldown() {
		  String c = "&c&m------------------------------------------------";
		  String c2 = "&c  You can only use this command every 60 seconds!";
		  String c3 = "&c&m------------------------------------------------";
		  return c + "\n" + c2 + "\n" + c3;
	  } 
	  
	  public static String translate(String message) {
	 	  return ChatColor.translateAlternateColorCodes('&', message);
	  }
	  
	  public static void paginateSearch(Player sender, ArrayList<TextComponent> list, int page, int countAll) {
          int contentLinesPerPage = 5;
          int totalPageCount = 1;
    
          if ((list.size() % contentLinesPerPage) == 0) {
            if (list.size() > 0) {
                totalPageCount = list.size() / contentLinesPerPage;
            }    
          } else {
            totalPageCount = (list.size() / contentLinesPerPage) + 1;
          }
    
          if (page <= totalPageCount) {
        	sender.sendMessage(" ");
            sender.sendMessage(Utils.translate("&d&lSearch Results &a&l(&e&lPage " + page + " &e&lof " + totalPageCount + "&a&l)"));    
    
            int i = 0, k = 0;
            page--;
    
            for (TextComponent entry : list) {
                 k++;
                 if ((((page * contentLinesPerPage) + i + 1) == k) && (k != ((page * contentLinesPerPage) + contentLinesPerPage + 1))) {
                     i++;
                     sender.spigot().sendMessage(entry);
                 }
             }
            sender.sendMessage(" ");

          } else {
            sender.sendMessage(ChatColor.RED + "There are only " + ChatColor.YELLOW + totalPageCount + ChatColor.RED + " pages!");
          }
      }
	  
	  public static void paginate(Player sender, ArrayList<TextComponent> list, int page, int countAll) {
          int contentLinesPerPage = 5;
          int totalPageCount = 1;
    
          if ((list.size() % contentLinesPerPage) == 0) {
            if (list.size() > 0) {
                totalPageCount = list.size() / contentLinesPerPage;
            }    
          } else {
            totalPageCount = (list.size() / contentLinesPerPage) + 1;
          }
    
          if (page <= totalPageCount) {
        	sender.sendMessage(" ");
            sender.sendMessage(Utils.translate("&d&lChat Reports &a&l(&e&lPage " + page + " &e&lof " + totalPageCount + "&a&l)"));    
    
            int i = 0, k = 0;
            page--;
    
            for (TextComponent entry : list) {
                 k++;
                 if ((((page * contentLinesPerPage) + i + 1) == k) && (k != ((page * contentLinesPerPage) + contentLinesPerPage + 1))) {
                     i++;
                     sender.spigot().sendMessage(entry);
                 }
             }
            sender.sendMessage(" ");

          } else {
            sender.sendMessage(ChatColor.RED + "There are only " + ChatColor.YELLOW + totalPageCount + ChatColor.RED + " pages!");
          }
      }
	  
	 public static boolean isFirstDayOfTheMonth(Date dateToday){
		Calendar c = new GregorianCalendar();
		c.setTime(dateToday);

		if (c.get(Calendar.DAY_OF_MONTH) == 1) {
		   return true;
		}
		return false;
	 }
	  

}
