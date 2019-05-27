package com.stest.leetcode;

import java.util.*;

public class Solution {
    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        StringBuilder num1 = new StringBuilder("");
        StringBuilder num2 = new StringBuilder("");
        int i = 1;
        int j = 1;
        while (l1 != null) {
            if (l1.val >= 0) {
                num1.insert(0, l1.val);
                l1 = l1.next;
                i++;
            }
        }
        while (l2 != null) {
            if (l2.val >= 0) {
                num2.insert(0, l2.val);
                l2 = l2.next;
                j++;
            }

        }
        ListNode node = null;
        ListNode cur = null;
        int len1 = num1.length();
        int len2 = num2.length();
        int max = len1 > len2 ? len1 : len2;
        int k = 1;
        int tmp = 0;

        while (k <= max) {
            int m = 0;
            int n = 0;
            if (k <= len1) {
                m = num1.charAt(len1 - k) - 48;
            }
            if (k <= len2) {
                n = num2.charAt(len2 - k) - 48;
            }
            int sum = m + n + tmp;

            int bit = sum % 10;
            if (node == null) {
                node = new ListNode(bit);
                cur = node;
            } else {
                cur.next = new ListNode(bit);
                cur = cur.next;
                if (tmp != 0) {
                    tmp = 0;
                }
            }

            if (sum >= 10) {
                tmp = 1;
            }
            k++;

        }

        if (tmp != 0) {
            cur.next = new ListNode(tmp);
        }
        return node;

    }


    public int maxSubArray(int[] nums) {
        int size = nums.length;
        int max = nums[0];
        for (int i = 0; i < size; i++) {
            int tmp = nums[i];
            int tmpMax = nums[i];
            for (int j = i + 1; j < size; j++) {
                tmp += nums[j];

                if (tmpMax < tmp) {
                    tmpMax = tmp;
                }
            }
            if (max < tmpMax) {
                max = tmpMax;
            }
        }

        return max;
    }


    public int lengthOfLongestSubstring(String s) {
        int max = 0;
        int size = s.length();
        HashSet<Character> set = new HashSet<Character>();
        for (int i = 0; i < size; i++) {
            for (int j = i; j < size; j++) {
                if (set.size() == 0) {
                    set.add(s.charAt(j));
                } else {
                    if (!set.contains(s.charAt(j))) {
                        set.add(s.charAt(j));
                    } else {
                        break;
                    }
                }
            }
            if (max == 0) {
                max = set.size();
            } else if (max < set.size()) {
                max = set.size();
            }
            set.clear();
            if (size - i - 1 <= max) {
                break;
            }
        }

        return max;
    }

    /**
     * 无序
     *
     * @param nums1
     * @return
     */
    public double findMedianSortedArrays1(int[] nums1) {
        int size = nums1.length;
        PriorityQueue<Integer> p1 = new PriorityQueue<Integer>(size, new Comparator<Integer>() {
            public int compare(Integer o1, Integer o2) {
                return o2 - o1;
            }
        });
        PriorityQueue<Integer> p2 = new PriorityQueue<Integer>(size, new Comparator<Integer>() {
            public int compare(Integer o1, Integer o2) {
                return o1 - o2;
            }
        });

        int i = 0, j = 0;
        int tmp = nums1[0];
        while (i < size) {
            if (nums1[i] <= tmp) {
                p1.add(nums1[i]);
            } else {
                p2.add(nums1[i]);
            }
            if (p1.size() != 0 && p1.size() - p2.size() > 1) {
                tmp = p1.poll();
                p2.add(tmp);
            } else if (p2.size() != 0 && p2.size() - p1.size() > 1) {
                tmp = p2.poll();
                p1.add(tmp);
            }
            i++;
        }
        if (p1.size() != 0 && p1.size() == p2.size()) {
            return (p1.peek() + p2.peek()) * 1.0 / 2;
        } else if (Math.abs(p1.size() - p2.size()) == 1) {
            return tmp;
        }
        return 0;
    }


    /**
     * 有序
     *
     * @param nums1
     * @param nums2
     * @return
     */
    public double findMedianSortedArrays(int[] nums1, int[] nums2) {
        int i = 0, j = 0;
        int size = nums1.length + nums2.length;
        int[] arr = new int[size];
        int k = 0;
        while (true) {
            if (i < nums1.length && j < nums2.length) {

                if (nums1[i] < nums2[j]) {
                    arr[k++] = nums1[i];
                    i++;
                } else if (nums1[i] >= nums2[j]) {
                    arr[k++] = nums2[j];
                    j++;
                }
            } else {
                while (i < nums1.length) {
                    arr[k++] = nums1[i];
                    i++;
                }
                while (j < nums2.length) {
                    arr[k++] = nums2[j];
                    j++;
                }

            }
            if (k == size) {
                break;
            }
        }

        if (arr.length % 2 == 0) {
            return (arr[arr.length / 2 - 1] + arr[arr.length / 2]) * 1.0 / 2;
        } else {
            return arr[arr.length / 2];
        }

    }

    public String longestPalindrome1(String s) {
        int size = s.length();
        String max = "";

        for (int m = 0; m < size; m++) {
            int i = m, j = size - 1;
            boolean flag = false;
            int start = 0;
            int end = 0;
            String tmp = "";
            while (i <= j) {
                if (s.charAt(i) != s.charAt(j)) {
                    flag = false;
                    j--;
                    if (start != 0 || end != 0) {
                        start = 0;
                        end = 0;
                    }
                } else {
                    if (!flag) {
                        start = i;
                        end = j;
                        flag = true;
                    }
                    i++;
                    j--;
                }

            }
            if (flag) {
                tmp = s.substring(start, end + 1);
            }
            if (tmp.length() > max.length()) {
                max = tmp;
            }
        }


        return max;
    }


    public String longestPalindrome(String s) {

        String max = "";
        int size = s.length();
        if (size <= 1) {
            return s;
        }
        for (int i = 0; i < size; i++) {
            int tmp = size;
            while (i <= tmp && tmp > 0) {
                String substring = s.substring(i, tmp);
                int m = 0, n = substring.length() - 1;
                boolean flag = false;
                while (m <= n) {
                    if (m == n) {
                        flag = true;
                        break;
                    }
                    if (substring.charAt(m) == substring.charAt(n)) {
                        m++;
                        n--;
                        flag = true;
                    } else {
                        flag = false;
                        break;
                    }
                }
                if (flag) {
                    if (substring.length() > max.length()) {
                        max = substring;
                        if (s.length() - max.length() <= 1) {
                            return max;
                        }
                    }
                }
                tmp--;
            }
        }
        return max;
    }

    /**
     * 错的
     *
     * @param s
     * @return
     */
    public String longestPalindrome2(String s) {
        String max = "";
        for (int i = 0; i < s.length(); i++) {
            int m = i, n = i;
            String tmp = "";
            boolean flag = false;
            while (m >= 0 || n < s.length()) {
                if (m < 0) {
                    if (s.charAt(i) == s.charAt(n)) {
                        n++;
                        flag = true;
                        break;
                    }
                }
                if (n >= s.length()) {
                    if (s.charAt(i) == s.charAt(m)) {
                        m--;
                        flag = true;
                        break;
                    }
                }
                if (s.charAt(m) == s.charAt(n)) {
                    m--;
                    n++;
                    flag = true;
                } else {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                m++;
                n--;
                String substring = s.substring(m, n + 1);
                if (tmp.length() < substring.length()) {
                    tmp = substring;
                }
            }
            if (max.length() < tmp.length()) {
                max = tmp;
            }

        }
        return max;
    }

    public String longestPalindrome3(String s) {
        //先判断是否为空或者长度小于1
        //把||写成了&&害我找了好久都不知道错在哪...
        if (s == null || s.length() < 1) {
            return "";
        }
        int left = 0;//用来记录子串的起始位置
        int right = 0;//用来记录子串的末尾位置

        for (int i = 0; i < s.length(); i++) {
            //通过findMore这个方法来拓展
            //bab这种情况
            int t1 = findMore(s, i, i);//bab这种情况
            //abba这种情况
            int t2 = findMore(s, i, i + 1);

            //选出比较长的那个
            int max = Math.max(t1, t2);
            if (max > right - left) {
                left = i - (max - 1) / 2;
                right = i + max / 2;
            }
        }
        return s.substring(left, right + 1);
    }

    public int findMore(String s, int left, int right) {
        while (left >= 0 && right < s.length()
                && s.charAt(left) == s.charAt(right)) {
            left--;
            right++;
        }
        return right - left - 1;
    }


    public int reverse(int x) {
        StringBuilder s = new StringBuilder("");
        boolean tag = x > 0;
        int num = Math.abs(x);
        if (num == 0) {
            return 0;
        }
        if (x > Integer.MAX_VALUE || x < Integer.MIN_VALUE) {
            return 0;
        }

        boolean start = true;
        while (num > 0) {
            int bit = num % 10;
            if (start && bit == 0) {
                start = false;
            } else {
                s.append(bit);
                start = false;
            }
            num /= 10;
        }
        try {
            int value = Integer.valueOf(s.toString());
            if (value > Integer.MAX_VALUE) {
                return 0;
            } else {
                return x > 0 ? value : -value;
            }
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 1 --- 49
     * - --- 45
     *
     * @param str
     * @return
     */
    public int myAtoi(String str) {
        List<String> list = Arrays.asList(str.split(" "));
        if (list.contains("+") || list.contains("-") || str.contains("+-")) {
            return 0;
        }
        StringBuilder num = new StringBuilder("");
        boolean tag = false;
        boolean isNum = false;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if ((c == 45 || c == 43) && !isNum) {
                tag = c == 45;
                continue;
            }
            if ((c > 47 && c < 58)) {
                isNum = true;
                num.append(c);
            } else if (c == 32) {
                continue;
            } else if (!isNum) {
                return 0;
            } else if (isNum) {
                break;
            }
        }

        if (num.length() == 0) {
            return 0;
        }
        try {
            Integer integer = Integer.valueOf(num.toString());
            if (tag) {
                return -integer;
            } else {
                return integer;
            }
        } catch (
                NumberFormatException e) {
            if (tag) {
                return Integer.MIN_VALUE;
            } else {
                return Integer.MAX_VALUE;
            }
        } catch (
                Exception e) {
            return 0;
        }

    }


    public int myAtoi1(String str) {
        StringBuilder s = new StringBuilder("");
        String tag = "";
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c == 32) {
                if (!"".equals(tag) && s.length() == 0) {
                    return 0;
                } else if (s.length() != 0) {
                    break;
                }
                continue;
            }
            if (c == 43 || c == 45) {
                if (!"".equals(tag) && s.length() == 0) {
                    return 0;
                }
                if ("".equals(tag) && s.length() != 0) {
                    return 0;
                }
                if (!"".equals(tag) && s.length() != 0) {
                    break;
                }

                tag = tag + c;
                continue;
            }
            if (c > 47 && c < 58) {
                s.append(c);
                continue;
            } else if (s.length() == 0) {
                return 0;
            }
            if (s.length() != 0) {
                break;
            }
        }

        if (s.length() == 0) {
            return 0;
        }

        try {
            int integer = Integer.parseInt(s.toString());
            if ("-".equals(tag)) {
                return -integer;
            } else {
                return integer;
            }
        } catch (
                NumberFormatException e) {
            if ("-".equals(tag)) {
                return Integer.MIN_VALUE;
            } else {
                return Integer.MAX_VALUE;
            }
        } catch (
                Exception e) {
            return 0;
        }

    }


    public int[] maxSlidingWindow(int[] nums, int k) {
        int size = nums.length;
        int[] result = new int[size - k + 1];


        int index = 0;
        for (int i = 0; i < size - k + 1; i++) {
            int max = nums[i];
            int j;
            for (j = i + 1; j < i + k; j++) {
                if (max < nums[j]) {
                    max = nums[j];
                }
            }
            result[index++] = max;
        }

        return result;
    }


    public ListNode removeNthFromEnd(ListNode head, int n) {
        ListNode cur = head;
        int size = 0;
        while (cur != null) {
            cur = cur.next;
            size++;
        }

        if (n == size) {
            return head.next;
        }
        cur = head.next;
        ListNode pre = head;
        int count = 1;
        while (cur != null) {
            count++;
            if (count == size) {
                pre.next = null;
                return head;
            } else if (count == (size - n + 1)) {
                pre.next = cur.next;
                return head;
            }
            cur = cur.next;
            pre = pre.next;

        }
        return null;

    }


    public List<List<Integer>> threeSum(int[] nums) {
        Arrays.sort(nums);
        int size = nums.length;
        List<List<Integer>> res = new ArrayList<List<Integer>>();
        if (size < 3) {
            return res;
        }

        if (size == 3) {
            if (nums[0] + nums[1] + nums[2] != 0) {
                return res;
            } else {
                ArrayList<Integer> list = new ArrayList<Integer>();
                list.add(nums[0]);
                list.add(nums[1]);
                list.add(nums[2]);
                res.add(list);
                return res;
            }
        }

        List<String> str = new ArrayList<String>();
        for (int i = 0; i < size - 2; i++) {
            if (i == 0 || (i > 0 && nums[i] != nums[i - 1])) {
                for (int j = i + 1; j < size - 1; j++) {
                    for (int k = j + 1; k < size; k++) {
                        if (nums[j] + nums[k] == -nums[i]) {
                            List<Integer> list = new ArrayList<Integer>();
                            list.add(nums[i]);
                            list.add(nums[j]);
                            list.add(nums[k]);
                            Collections.sort(list, new Comparator<Integer>() {
                                public int compare(Integer o1, Integer o2) {
                                    return o1 - o2;
                                }
                            });
                            String s = list.toString();
                            if (!str.contains(s)) {
                                res.add(list);
                                str.add(s);
                            }
                        }
                    }
                }
            }
        }
        return res;
    }

    public List<List<Integer>> threeSum1(int[] nums) {
        Arrays.sort(nums);
        List<List<Integer>> ls = new ArrayList<List<Integer>>();

        for (int i = 0; i < nums.length - 2; i++) {
            if (i == 0 || (i > 0 && nums[i] != nums[i - 1])) {  // 跳过可能重复的答案

                int l = i + 1, r = nums.length - 1, sum = 0 - nums[i];
                while (l < r) {
                    if (nums[l] + nums[r] == sum) {
                        ls.add(Arrays.asList(nums[i], nums[l], nums[r]));
                        while (l < r && nums[l] == nums[l + 1]) l++;
                        while (l < r && nums[r] == nums[r - 1]) r--;
                        l++;
                        r--;
                    } else if (nums[l] + nums[r] < sum) {
                        while (l < r && nums[l] == nums[l + 1]) l++;   // 跳过重复值
                        l++;
                    } else {
                        while (l < r && nums[r] == nums[r - 1]) r--;
                        r--;
                    }
                }
            }
        }
        return ls;
    }


    public int[] sort(int[] nums) {
        for (int i = 0; i < nums.length - 1; i++) {
            int tmp = i;
            for (int j = i + 1; j < nums.length; j++) {
                if (nums[tmp] > nums[j]) {
                    tmp = j;
                }
            }

            if (tmp != i) {
                int swap = nums[i];
                nums[i] = nums[tmp];
                nums[tmp] = swap;
            }
        }

        return nums;
    }

    public boolean isValid(String s) {
        Stack<Character> stack = new Stack<Character>();
        HashMap<Character, Character> map = new HashMap<Character, Character>();
        map.put('{', '}');
        map.put('[', ']');
        map.put('(', ')');
        HashSet<Character> in = new HashSet<Character>();
        in.add('{');
        in.add('(');
        in.add('[');
        HashSet<Character> out = new HashSet<Character>();
        out.add('}');
        out.add(']');
        out.add(')');

        int size = s.length();
        for (int i = 0; i < size; i++) {
            char c = s.charAt(i);
            if (stack.empty()) {
                stack.push(c);
            } else {
                if (in.contains(c)) {
                    stack.push(c);
                } else if (out.contains(c)) {
                    if (map.containsKey(stack.peek())) {
                        if (s.charAt(i) == map.get(stack.peek())) {
                            stack.pop();
                        } else {
                            return false;
                        }
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            }
        }
        return stack.empty();
    }

    public ListNode mergeTwoLists(ListNode l1, ListNode l2) {
        if (l1 == null && l2 == null) {
            return null;
        }
        if (l1 == null || l2 == null) {
            return l1 == null ? l2 : l1;
        }
        ListNode small = l1.val <= l2.val ? l1 : l2;
        ListNode large = l1.val > l2.val ? l1 : l2;

        ListNode cur = new ListNode(small.val);
        ListNode pre = cur;
        small = small.next;
        while (true) {
            if ((small != null && large != null) && (small.val <= large.val)) {
                ListNode e = new ListNode(small.val);
                e.next = null;
                pre.next = e;
                pre = pre.next;
                small = small.next;
            } else if ((small != null && large != null) && (small.val > large.val)) {
                ListNode e = new ListNode(large.val);
                e.next = null;
                pre.next = e;
                pre = pre.next;
                large = large.next;
            } else if (small != null) {
                pre.next = small;
                break;
            } else {
                pre.next = large;
                break;
            }

        }
        return cur;
    }

    public List<String> generateParenthesis(int n) {
        Stack<String> s = new Stack<String>();
        List<String> list = new ArrayList<String>();
        for (int i = 1; i <= n; i++) {
            StringBuffer buffer = new StringBuffer("");
            while (buffer.length() < n * 2) {
                for (int j = 0; j < i; j++) {
                    s.push("(");
                    buffer.append("(");
                }
                while (!s.empty()) {
                    s.pop();
                    buffer.append(")");
                }
            }
            list.add(buffer.toString());
        }
        return list;

    }

    public int romanToInt(String s) {
        HashMap<Character, Integer> map = new HashMap<Character, Integer>();
        map.put('I', 1);
        map.put('V', 5);
        map.put('X', 10);
        map.put('L', 50);
        map.put('C', 100);
        map.put('D', 500);
        map.put('M', 1000);
        HashMap<String, Integer> map1 = new HashMap<String, Integer>();
        map1.put("IV",5);
        map1.put("IX",5);
        map1.put("XC",50);
        map1.put("XL",50);
        map1.put("CM",500);
        map1.put("CD",500);

        int sum = 0;
        int size = s.length();
        for (int i =0; i <size; i++) {
            char c = s.charAt(i);
            if(i == size -1){
                sum += map.get(c);
                break;
            }
            String ss = c+""+ s.charAt(i+1);
            if(map1.containsKey(ss)){
                sum += map1.get(ss);
                i++;
            }else {
               sum += map.get(c);
            }

        }
        return sum;

    }

    public ListNode middleNode(ListNode head) {
       /* ListNode l1 = head.next;
        if(l1 == null){
            return head;
        }
        ListNode l2 = l1.next;
        if(l2 ==null || l2.next == null){
            return l1;
        }
        ListNode l3 = head;
        int count = 1;
        while(l2.next != null){
            count++;
            l3 = l3.next;
            l1 = l1.next;
            l2 = l2.next;
            l2 = l2.next;
        }
        if(l2 != null){
            return l1.next;
        }else{
            if(count %2 ==0){
                return l1;
            }else{
                return l3;
            }
        }*/

       ListNode l1 = head;
       if(l1.next == null){
           return head;
       }
       ListNode l2 = l1.next;
       if(l2.next == null){
           return l2;
       }
       while(true){
           l2 = l2.next;
           if(l2 == null){
               return l1.next;
           }
           l1 = l1.next;
           l2 = l2.next;
           if(l2 == null){
               return l1;
           }
       }


    }

    public static void main(String[] args) throws Exception {
        /*ListNode l1 = new ListNode(9);

        ListNode l2 = new ListNode(1);
        ListNode cur = l2;
        cur.next = new ListNode(9);
        cur = cur.next;
        cur.next = new ListNode(9);
        cur = cur.next;
        cur.next = new ListNode(9);
        cur = cur.next;
        cur.next = new ListNode(9);
        cur = cur.next;
        cur.next = new ListNode(9);
        cur = cur.next;
        cur.next = new ListNode(9);
        cur = cur.next;
        cur.next = new ListNode(9);
        cur = cur.next;
        cur.next = new ListNode(9);
        cur = cur.next;
        cur.next = new ListNode(9);*/

       /* ListNode l1 = new ListNode(2);
        ListNode cur1 = l1;
        cur1.next = new ListNode(4);
        cur1 = cur1.next;
        cur1.next = new ListNode(3);
        cur1 = cur1.next;

        ListNode l2 = new ListNode(5);
        ListNode cur = l2;
        cur.next = new ListNode(6);
        cur = cur.next;
        cur.next = new ListNode(4);
        cur = cur.next;*/

      /* ListNode l1 = new ListNode(1);

        ListNode l2 = new ListNode(9);
        ListNode cur = l2;
        cur.next = new ListNode(9);
*/

        /*int[] nums1 = new int[]{7,1, 3,2,5};



        double res = new Solution().findMedianSortedArrays1(nums1);
        System.out.print(res);*/
       /* String max = new Solution().longestPalindrome3("ccbdd");
        System.out.print(max);*/
       /* int reverse = new Solution().reverse(102);
        System.out.print(reverse);*/
        //


        //new Solution().myAtoi1(" +4 -tt -2");
       /* int[] nums = new int[]{1, 3, -1, -3, 5, 3, 6, 7};
        new Solution().maxSlidingWindow(nums, 3);*/

       /* ListNode l1 = new ListNode(1);
        ListNode cur1 = l1;
        cur1.next = new ListNode(2);
        cur1 = cur1.next;
        cur1.next = new ListNode(3);
        cur1 = cur1.next;
        cur1.next = new ListNode(4);
        cur1 = cur1.next;
        cur1.next = new ListNode(5);
        cur1 = cur1.next;
        new Solution().removeNthFromEnd(l1, 1);*/

      /* int[] nums = new int[]{-1, 0, 1, 2, -1, -4};
       new Solution().sort(nums);*/

        //new Solution().isValid("{[]}");
       /*  ListNode l1 = new ListNode(1);
        ListNode cur = l1;
        cur.next = new ListNode(3);
        cur = cur.next;
        cur.next = new ListNode(4);


        ListNode l2 = new ListNode(1);
        ListNode cur2 = l2;
        cur2.next = new ListNode(2);
        cur2 = cur2.next;
        cur2.next = new ListNode(4);

        new Solution().mergeTwoLists(l1,l2);*/
        //new Solution().generateParenthesis(3);
        //new Solution().romanToInt("LVIII");

        ListNode l1 = new ListNode(1);
        ListNode cur1 = l1;
        cur1.next = new ListNode(2);
        cur1 = cur1.next;
        cur1.next = new ListNode(3);
        cur1 = cur1.next;
        cur1.next = new ListNode(4);

        new Solution().middleNode(l1);
    }
}
