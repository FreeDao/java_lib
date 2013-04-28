/*****************************************************************************
 *
 *                      AIRBIQUITY PROPRIETARY INFORMATION
 *
 *          The information contained herein is proprietary to Airbiquity
 *           and shall not be reproduced or disclosed in whole or in part
 *                    or used for any design or manufacture
 *              without direct written authorization from Airbiquity.
 *
 *            Copyright (c) 2012 by Airbiquity.  All rights reserved.
 *            
 *                   @author 
 *                   Jack Li
 *
 *****************************************************************************/
package com.airbiquity.mcs.tcpip;

import com.airbiquity.mcs.common.MCSException;

/**
 * Provides circular buffer for storing data of any type.
 */
public class DataQueueSentIPPacket {
   private int qMaxSize;// max queue size
   private int fp = 0;  // front pointer
   private int rp = 0;  // rear pointer
   private int qs = 0;  // size of queue
   private SentIPPacket[] q;  // actual queue

   /**
    * 
    * @param size
    */
   public DataQueueSentIPPacket(int size) {
      qMaxSize = size;
      fp = 0;
      rp = 0;
      qs = 0;
      q = new SentIPPacket[qMaxSize];
   }

   /**
    * 
    * @return
    * @throws MCSException
    */
   public SentIPPacket delete() throws MCSException {
      if (!emptyq()) {
         qs--;
         fp = (fp + 1)%qMaxSize;
         return q[fp];
      }
      else {
         throw new MCSException("DataQueueSentIPPacket - Queue underflow");
      }
   }

   /**
    * 
    * @return
    * @throws MCSException
    */
   public SentIPPacket get() throws MCSException {
      if (!emptyq()) {
         return q[(fp + 1)%qMaxSize];
      }
      else {
         throw new MCSException("DataQueueSentIPPacket - Queue underflow");
      }
   }

   /**
    * 
    * @param i
    * @return
    * @throws MCSException
    */
   public SentIPPacket get(int i) throws MCSException {
      if (qs > i) {
         return q[(fp + 1 + i)%qMaxSize];
      }
      else {
         throw new MCSException("DataQueueSentIPPacket - Queue underflow - index too high");
      }
   }

   /**
    * 
    * @param o
    * @throws MCSException
    */
   public void insert(SentIPPacket o) throws MCSException {
      if (!fullq()) {
         qs++;
         rp = (rp + 1)%qMaxSize;
         q[rp] = o;
      }
      else
          throw new MCSException("DataQueueSentIPPacket - Overflow");
   }
   
   /**
    * 
    * @param o
    * @return
    */
   public boolean contains(SentIPPacket o) {
       if( qs == 0 ) {
           return false;
       }
       if( fp < rp ) {
           return contains(o, fp + 1, rp);
       }
       return contains(o, fp + 1, qMaxSize - 1) || contains(o, 0, rp);
   }
   
   /**
    * 
    * @param o
    * @param start
    * @param end
    * @return
    */
   private boolean contains(SentIPPacket o, int start, int end) {
       for( int i = start; i <= end; i++ ) {
           if(q[i] == o) {
               return true;
           }
       }
       return false;
   }

   /**
    * 
    * @return
    */
   public boolean emptyq() {
      return qs == 0;
   }

   /**
    * 
    * @return
    */
   public boolean fullq() {
      return qs == qMaxSize;
   }
   
   /**
    * 
    * @return
    */
   public int size() {
       return qs;
   }
}
