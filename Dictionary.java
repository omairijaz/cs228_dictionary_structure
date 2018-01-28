package edu.iastate.cs228.hw4;

import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.File;

/**
 * @author Omair Ijaz
 * 
 *         An application class
 */
public class Dictionary {
	public static void main(String[] args) throws FileNotFoundException {

		System.out.println("Notes on the output format of an entry tree:"
				+ "\nA node with Character o (key) and String Larry (value) is shown in the form: o->Larry"
				+ "\nA child node is shown with right indentation below its parent node."
				+ "\nThe children of a node are shown at the same level of indentation.\n");

		File inputFile = new File(args[0]);// "infile.txt"
		Scanner lineScanner = new Scanner(inputFile);
		EntryTree<Character, String> tree = new EntryTree<Character, String>();

		while (lineScanner.hasNextLine()) {
			// our new scanner that takes in our line
			Scanner in = new Scanner(lineScanner.nextLine());
			String method = in.next();/// our method name is first string

			if (method.equals("showTree")) {// these are our else if cases,
											// starting with showTree
				System.out.println("Command: showTree \n");
				tree.showTree();
				System.out.print("\n");
			} else if (method.equals("add")) {
				String keyarr = in.next();// we use our helper method to change
											// this to Character[]
				String value = in.next();
				System.out.println("Command: add " + keyarr + " " + value);
				boolean result = tree.add(toCharacterArray(keyarr), value);
				System.out.println("Result from an add: " + result + "\n");
			} else if (method.equals("search")) {
				String keyarr = in.next();
				System.out.println("Command: search " + keyarr);
				String value = tree.search(toCharacterArray(keyarr));
				System.out.println("Result from search: " + value + "\n");
			} else if (method.equals("prefix")) {
				String keyarr = in.next();
				System.out.println("Command: prefix " + keyarr);
				Character[] arr = tree.prefix(toCharacterArray(keyarr));
				System.out.println("Result from prefix: " + convertToString(arr) + "\n");
			} else {// this is our remove method
				String keyarr = in.next();
				System.out.println("Command: remove " + keyarr);
				String value = tree.remove(toCharacterArray(keyarr));
				System.out.println("Result from remove: " + value + "\n");
			}
			in.close();
		}
		lineScanner.close();
	}

	/**
	 * Converts a string to a Character Array.
	 * 
	 * @param str
	 *            String to be converted to a Character Array
	 * @return Character Array representation of str
	 */
	public static Character[] toCharacterArray(String str) {
		if (str == null)
			return null;

		Character[] arr = new Character[str.length()];
		for (int i = 0; i < str.length(); i++) {
			arr[i] = new Character(str.charAt(i));// char is converted to
													// wrapper class
		}
		return arr;
	}

	/**
	 * Converts a Character array to a string
	 * 
	 * @param arr
	 *            Character array to be converted
	 * @return str A string representation of our Character array
	 */
	public static String convertToString(Character[] arr) {
		if (arr == null) {
			return null;
		}
		String str = "";
		for (int i = 0; i < arr.length; i++) {
			str += arr[i];
		}
		return str;
	}
}
