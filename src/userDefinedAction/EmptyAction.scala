package userDefinedAction

import ipc.RepeatClient

/**
 * Empty action that does nothing.
 */
class EmptyAction() extends UserDefinedAction {
  
  override def execute(controller : RepeatClient, activation : Activation) : Unit = {
    // Intentionally left empty
  }
}