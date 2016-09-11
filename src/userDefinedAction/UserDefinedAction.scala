package userDefinedAction

import ipc.RepeatClient

/**
 * Abstract class to represent a user defined action.
 */
abstract class UserDefinedAction() {
  def execute(controller : RepeatClient, activation : Activation) : Unit;
}