package edu.iastate.cs228.hw4;

import static org.junit.Assert.*;
import org.junit.Test; 

/**
 * @author Omair Ijaz
 * 
 * Junit Testing of EntryTree
 */

public class EntryTreeTest {

	EntryTree<Character, String> tree = new EntryTree<Character, String>();//tree to test on
	
	//the root has to have null key
	@Test
	public void constructorTest(){
		assertNull(tree.root.key);
	}
	
	//the root has to have null value
	@Test
	public void constructorTest2(){
		assertNull(tree.root.value);
	}
	
	//adding a null element throws an exception
	@Test(expected = NullPointerException.class) 
	public void addTest1(){
		Character[] keyarr = {'T',null};
		tree.add(keyarr, "test");
	}
	
	//adding a null value returns false
	@Test 
	public void addTest2(){
		Character[] keyarr = {'e','d','i','t'};
		assertFalse(tree.add(keyarr, null));
	}
	
	//adding a keyarr of length zero returns false
	@Test 
	public void addTest3(){
		Character[] keyarr = new Character[0];
		assertFalse(tree.add(keyarr, "Test"));
	}
	
	//adding a null keyarr returns false
	@Test 
	public void addTest4(){
		Character[] keyarr = null;
		assertFalse(tree.add(keyarr, "Test"));
	}
	
	//adding correctly should return true
	@Test
	public void addTest5(){
		Character[] keyarr = {'e','d','i','t'};
		assertTrue(tree.add(keyarr, "change"));
	}
	
	//adding a same length array should change value of that node
	@Test
	public void addTest6(){
		Character[] keyarr = {'e','d','i','t'};
		tree.add(keyarr, "change");
		tree.add(keyarr, "revise");//our new value at node that contains 't'
		String expected = "revise";
		assertEquals(tree.search(keyarr),expected);
	}
	
	//checking the nodes after adding
	@Test
	public void addTest7(){
		Character[] keyarr = {'e','d','i','t'};
		tree.add(keyarr, "revise");
		Character[] keyarr2 = {'f','o','o'};
		tree.add(keyarr2,"bar");
		Character expected = 'f';
		assertEquals(tree.root.child.next.key, expected);//the sibling of 'e' should be 'f'
	}
	
	@Test
	public void addTest8(){
		Character[] keyarr = {'e','d','i','t'};
		tree.add(keyarr, "revise");
		Character expected = 'e';
		assertEquals(tree.root.child.key, expected);//the child of root should be 'e'
	}
	
	@Test
	public void addTest9(){
		Character[] keyarr = {'e','d','i','t'};
		tree.add(keyarr, "revise");
		assertNotNull(tree.root.child.key);//the child of root should be non-null
	}

		
	//return null if keyarr is null
	@Test
	public void searchTest1(){
		Character[] keyarr = null;
		assertNull(tree.search(keyarr));
	}
	
	//return null if keyarr length is zero
	@Test
	public void searchTest2(){
		Character[] keyarr = new Character[0];
		assertNull(tree.search(keyarr));
	}
	
	//throw an exception if there is a null element
	@Test(expected = NullPointerException.class)
	public void searchTest3(){
		Character[] keyarr = {'e','d','i','t',null};
		tree.search(keyarr);
	}
	
	//returning a value from given keyarr
	@Test
	public void searchTest4(){
		Character[] keyarr = {'e','d','i','t'};
		tree.add(keyarr, "revise");
		String expected = "revise";
		assertEquals(tree.search(keyarr), expected);
	}
	
	//tests a null keyarr for prefix
	@Test
	public void prefixTest1(){
		Character[] keyarr = null;
		assertNull(tree.prefix(keyarr));
	}
	
	//tests a keyarr of length zero for prefix
	@Test
	public void prefixTest2(){
		Character[] keyarr = new Character[0];
		assertNull(tree.prefix(keyarr));
	}
	
	//testing the basic functionality of prefix
	//prefix is also used in add
	@Test
	public void prefixTest3(){
		Character[] keyarr = {'e','d','i','t'};
		tree.add(keyarr, "revise");
		Character[] keyarr2 = {'e','d','i','t','e','d'};
		assertArrayEquals(tree.prefix(keyarr2),keyarr);
	}
	
	//now we are testing prefixlen
	@Test
	public void prefixTest4(){
		Character[] keyarr = {'e','d','i','t'};
		tree.add(keyarr, "revise");
		Character[] keyarr2 = {'e','d','i','t','e','d'};
		tree.prefix(keyarr2);
		assertEquals(tree.prefixlen, 4);//our longest prefix should be length 4
		//our prefixlen is calculated in my private helper method searchNodeAtEnd
	}
	
	//basic functionality test, we should return null if 
	//specified keyarr doesn't not exist in our tree
	@Test
	public void prefixTest5(){
		Character[] keyarr = {'e','d','i','t'};
		tree.add(keyarr, "revise");
		Character[] keyarr2 = {'f','o','o'};
		assertNull(tree.search(keyarr2));
	}

	//testing for null when removing a null keyarr
	@Test
	public void removeTest1(){
		Character[] keyarr = null;
		assertNull(tree.remove(keyarr));
	}

	//testing for null when removing a keyarr with length zero
	@Test
	public void removeTest2(){
		Character[] keyarr = new Character[0];
		assertNull(tree.remove(keyarr));
	}
	
	//trying to remove a keyarr with null element(s)
	@Test(expected = NullPointerException.class) 
	public void removeTest3(){
		Character[] keyarr = {'e','d','i','t',null};
		tree.remove(keyarr);
	}
	
	//returning null for a key entry that doesnt exist
	@Test
	public void removeTest4(){
		Character[] keyarr = {'e','d','i','t'};
		tree.add(keyarr, "revise");
		Character[] keyarr2 = {'e','d','i','t','e','d'};
		tree.add(keyarr2,"revised");
		String expected = "revised";
		assertEquals(tree.remove(keyarr2),expected);
	}
	
	//testing removal of nodes
	@Test
	public void removeTest5(){
		Character[] keyarr = {'e','d','i','t'};
		tree.add(keyarr, "revise");
		Character[] keyarr2 = {'e','d','i','t','e','d'};
		tree.add(keyarr2,"revised");
		tree.remove(keyarr);//should still contain edit
		assertArrayEquals(keyarr, tree.prefix(keyarr));//our tree should still contain edit
	}
	
	//testing more removal of nodes
	@Test
	public void removeTest6(){
		Character[] keyarr = {'e','d','i','t'};
		tree.add(keyarr, "revise");
		Character[] keyarr2 = {'e','d','i','t','e','d'};
		tree.add(keyarr2,"revised");
		tree.remove(keyarr2);//our tree should NOT contain edited anymore
		assertArrayEquals(keyarr,tree.prefix(keyarr2));//prefix of keyarr2 returns edit now 
	}	
	
	@Test
	public void removeTest7(){
		Character[] keyarr = {'e','d','i','t'};
		tree.add(keyarr, "revise");
		Character[] keyarr2 = {'e','d','i','t','e','d'};
		tree.add(keyarr2,"revised");
		Character[] keyarr3 = {'e','d','i','c','t'};
		tree.add(keyarr3,"order");
		Character[] toRemove = {'e','d','i','c'};
		tree.remove(toRemove);
		assertEquals(tree.search(keyarr3),"order");//we can't remove a node with a child
	}
	
	@Test
	public void removeTest8(){
		Character[] keyarr = {'e','d','i','t'};
		tree.add(keyarr, "revise");
		Character[] keyarr2 = {'e','d','i','t','e','d'};
		tree.add(keyarr2,"revised");
		Character[] keyarr3 = {'e','d','i','c','t'};
		tree.add(keyarr3,"order");
		tree.remove(keyarr3);
		assertNull(tree.search(keyarr3));//testing proper removal of edict
		//we shouldn't be able to find it anymore aka search returns null
	}
	
}