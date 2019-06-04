package com.stest.leetcode;

import lombok.val;

import java.util.Stack;

public class ListNodeSolution {

    //必须要引用变量带回来值,求值
    public static int currentNodeKth2Tail(ListNode head ,int k,ResultCache resultCache){
      if(head == null){
          return 0;
      }
      int i = currentNodeKth2Tail(head.next,k,resultCache)+1;
      if(i == k){
          resultCache.setValue(head.val);
      }
      return i;
    }

    //非递归的快慢指针模式，慢指针就是结果，快指针先走K步 ，他们相差K
    //和删除的快慢指针有些许不同
    public static ListNode currentNodeKth2Tail1(ListNode head, int n) {
        ListNode first = head;
        ListNode second = head;
        for (int i = 1; i <= n ; i++) {
            if(first != null){
                first = first.next;
            }else {
                return null;
            }

        }
        while (first != null) {
            first = first.next;
            second = second.next;
        }
        return second;
    }


    /**
     * 快慢指针 一个一步一个两步
     * @param pHead
     * @return
     */
    public boolean isEntryNodeOfLoop(ListNode pHead){
        ListNode fast = pHead;
        if(fast == null || fast.next == null){
            return false;
        }

        ListNode slow = pHead;
        while(true){
            if(fast == null || fast.next ==null){
                return false;
            }
            fast = fast.next;

            fast = fast.next;
            slow = slow.next;
            if(fast == slow){
                return true;
            }
        }
    }

    public ListNode EntryNodeOfLoop(ListNode pHead){
        ListNode fast = pHead;
        if(fast == null || fast.next == null){
            return null;
        }

        ListNode slow = pHead;
        while(true){
            if(fast == null || fast.next == null){
                return null;
            }
            fast = fast.next.next;
            slow = slow.next;
            if(fast == slow){
                break;
            }
        }
        slow = pHead;
        while(slow != fast){
            slow = slow.next;
            fast = fast.next;
        }
        return slow;
    }

    public int EntryNodeOfLoopLen(ListNode pHead){
        ListNode fast = pHead;
        if(fast == null || fast.next == null){
            return 0;
        }

        ListNode slow = pHead;
        int len1 = 0;
        int len2 = 0;
        while(true){
            if(fast == null || fast.next ==null){
                return 0;
            }
            fast = fast.next.next;
            len1 +=2;
            slow = slow.next;
            len2++;
            if(fast == slow){
                return len1-len2;
            }
        }
    }

    /**
     * 在正常不变态的整型范围内的计算，不考虑溢出，这个的效率还是很高的，但是如果是要有溢出的测试用例的，还是之前做的字符串的版本，自己实现加法，进行进一是最稳妥的
     * @param l1
     * @param l2
     * @return
     */
    public static ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        int n1 = getNumbers(l1);
        int n2 = getNumbers(l2);
        int n = n1+n2;
        ListNode head = new ListNode(n % 10);
        n /=10;
        while(n>0){
            head.next = new ListNode(n%10);
            head = head.next;
            n /=10;
        }
        return head;
    }

    public static int getNumbers(ListNode list){
        if(list.next == null){
            return list.val ;
        }
        return list.val + getNumbers(list.next)*10;
    }

    public static ListNode addTwoNumbers1(ListNode l1, ListNode l2) {
        ListNode ret = new ListNode(0);
        ListNode start = ret;
        boolean shouldUp = false;
        while (l1 != null || l2 != null) {
            int sum = 0;
            if (l1 != null) {
                sum += l1.val;
                l1 = l1.next;
            }
            if (l2 != null) {
                sum += l2.val;
                l2 = l2.next;
            }
            if (shouldUp) {
                sum = sum + 1;
            }
            ret.next = new ListNode(sum % 10);
            ret = ret.next;
            if (sum >= 10) {
                shouldUp = true;
            } else {
                shouldUp = false;
            }
        }
        if(shouldUp){
            ret.next = new ListNode(1);
        }
        return start.next;
    }

    public ListNode removeElements(ListNode head, int val) {
       ListNode cur = new ListNode(0);
       cur.next = head;
       ListNode tmp = cur;

       while(tmp.next != null){
           if(tmp.next.val == val){
               tmp.next = tmp.next.next;
           }else{
               tmp = tmp.next;
           }
       }
       return cur.next;
    }

    public static void reversePrint(ListNode head){
        if(head != null){
            reversePrint(head.next);
            System.out.print("  "+head.val+"  ");
        }
    }

   /* public static ListNode reverseList(ListNode head) {

    }*/

    public static void cursorForList(ListNode head,ListNode tmp){
        if(head.next != null){
            cursorForList(head.next,tmp);
            ListNode l = tmp;
            while(l.next != null){
                l = l.next;
            }
            l.next = new ListNode(head.val);
        }else {
            tmp.val = head.val;
        }
    }

    public static  ListNode reverseList(ListNode head) {
        Stack<Integer> s = new Stack<Integer>();
        if(head == null){
            return null;
        }

        ListNode tmp = head;
        while(tmp != null){
            s.push(tmp.val);
            tmp = tmp.next;
        }

        ListNode l = new ListNode(s.pop());
        ListNode result = l;
        while(!s.empty()){
            l.next = new ListNode(s.pop());
            l = l.next;
        }
        return result;

    }

    public static  ListNode reverseList1(ListNode head) {
        ListNode revert=null;
        while(head!=null){
            ListNode originNext=head.next;
            head.next=revert;
            revert=head;
            head=originNext;
        }

        return revert;
    }


    public static ListNode reverseList3(ListNode pHead){
        //如果没有结点或者只有一个结点直接返回pHead
        if(pHead==null || pHead.next == null){
            return pHead;
        }
        //保存当前结点的下一结点
        ListNode pNext = pHead.next;
        //打断当前结点的指针域
        pHead.next = null;
        //递归结束时reverseHead一定是新链表的头结点
        ListNode reverseHead = reverseList3(pNext);
        //修改指针域
        pNext.next = pHead;
        return reverseHead;
    }


    public static void main(String[] args){
        ListNode l1 = new ListNode(2);
        ListNode cur1 = l1;
        cur1.next = new ListNode(4);
        cur1 = cur1.next;
        cur1.next = new ListNode(3);

        ListNode l2 = new ListNode(5);
       /* ListNode cur = l2;
        cur.next = new ListNode(6);
        cur = cur.next;
        cur.next = new ListNode(4);*/

        //ListNode listNode = addTwoNumbers1(l1, l2);
       /* ListNode l = new ListNode(0);
        cursorForList(l1,l);*/
       reverseList3(l1);
    }
}
