package com.karumi.rosie.domain.usercase;

import org.junit.Test;

import static org.junit.Assert.*;

public class ClassUtilsTest {

  @Test
  public void testCanAssignTwoAnyObjects() throws Exception {
    assertTrue(ClassUtils.canAssign(String.class,String.class));
  }

  @Test
  public void testCanAssignTwoAnyObjectsWithHerenchy() throws Exception {
    assertTrue(ClassUtils.canAssign(AnyClass.class,SonOfAnyClass.class));
  }

  @Test
  public void testCanAssignTwoObjectsWithAPrimitiveObject() throws Exception {
    assertTrue(ClassUtils.canAssign(int.class,Integer.class));
  }

  @Test
  public void testCanAssignTwoObjectsWithAPrimitiveObjectBase() throws Exception {
    assertTrue(ClassUtils.canAssign(Integer.class,int.class));
  }

  private class AnyClass {
  }

  private class SonOfAnyClass extends AnyClass {
  }
}