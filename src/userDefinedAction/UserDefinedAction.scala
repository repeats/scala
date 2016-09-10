package userDefinedAction

import ipc.RepeatClient

abstract class UserDefinedAction() {
  def execute(controller : RepeatClient, activation : Activation) : Unit;
}