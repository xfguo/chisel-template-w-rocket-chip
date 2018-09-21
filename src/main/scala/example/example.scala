package example

import chisel3._
import chisel3.util._
import chisel3.experimental._
import freechips.rocketchip.util._


class example extends Module {
  val io = IO( new Bundle {
    val in_clock  = Input(Clock())
    val in_reset  = Input(Bool())
    val in_data   = Input(UInt(32.W))
    val out_clock = Input(Clock())
    val out_reset = Input(Bool())
    val out_data  = Output(UInt(32.W))
  } )

  val crossing = Module(new AsyncQueue(UInt(32.W)))
  
  crossing.io.enq_clock := io.in_clock
  crossing.io.enq_reset := io.in_reset

  crossing.io.deq_clock := io.out_clock
  crossing.io.deq_reset := io.out_reset
 
  crossing.io.enq.bits  := io.in_data
  crossing.io.enq.valid := true.B

  io.out_data := crossing.io.deq.bits
  crossing.io.deq.ready := true.B
}

object Generator extends App {
  chisel3.Driver.execute(args, () => new example)
}
