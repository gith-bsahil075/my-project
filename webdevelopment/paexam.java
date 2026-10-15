//merge sort
// public class paexam{
//     public static void conquer(int arr[], int si, int ei, int mid){
//         int merged[] = new int[ei - si+1];
//         int idx1 = si;
//         int idx2 = mid+1;
//         int x = 0;
//         while(idx1 <= mid && idx2 <= ei){
//             if(arr[idx1] <= arr[idx2]){
//                 merged[x++] = arr[idx1++];

//             }else{
//                 merged[x++] = arr[idx2++];
//             }
//         }
//         while(idx1 <= mid){
//            merged[x++] = arr[idx1++]; 
//         }
//         while(idx2 <= ei){
//             merged[x++] = arr[idx2++];
//         }
//         for(int i = 0, j = si; i < merged.length; i++, j++){
//             arr[j] = merged[i];
//         }
//     }
//     public static void divide(int arr[],int si, int ei){
//        if(si >= ei){
//         return;
//        } 
//       int mid = si+(ei - si)/2;
//       divide(arr,si,mid);
//       divide(arr,mid+1,ei);
//       conquer(arr,si,ei,mid);
//     }
// public static void main(String[] args){
//     int arr[] = {1,4,3,6,9,8};
//     int n = arr.length;
//     divide(arr, 0 , n-1);
//     for(int i = 0; i < n; i++){
//         System.out.println(arr[i] + " ");
//     }
//     System.out.println();
// }
// }
// public class paexam{
//     public static void conquer(int arr[], int si, int ei, int mid){
//         int merged[] = new int[ei - si+1];
//         int idx1 = si;
//         int idx2 = mid+1;
//         int x = 0;
//         while(idx1 <= mid && idx2 <= ei ){
//             if(arr[idx1] <= arr[idx2]){
//                 merged[x++] = arr[idx1++];
//             }else{
//                 merged[x++] = arr[idx2++];
//             }
//         }
//         while(idx1 <= mid){
//             merged[x++] = arr[idx1++];
//         }
//         while(idx2 <= ei){
//             merged[x++] = arr[idx2++];
//         }
//         for(int i = 0, j = s)
//     }
//     public static void divide(int arr[], int si, int ei){
//         if(si >= ei){
//             return;
//         }
//         int mid = si + (ei - si)/2;
//         System.out.println(si + "+" + mid);

//         divide(arr,si,mid);
//          System.out.println(mid+1 + "+" + ei);
//         divide(arr,mid+1, ei);
        
//     }
//     public static void main(String[] args){
//         int arr[] = {9,4,6,2,8,5};
//         int n = arr.length;
//         divide(arr,0,n-1);
       
//     }
// }
// public class paexam{
//     public static void conquer(int arr[],int si,int mid, int ei ){
//         int merged[] = new int[ei - si +1];
//         int idx1 = si;
//         int idx2 = mid+1;
//         int x = 0;
//         while(idx1 <= mid && idx2 <= ei){
//             if(arr[idx1] <= arr[idx2]){
//                 merged[x++] = arr[idx1++];
//             }else{
//                 merged[x++] = arr[idx2++];
//             }
//         }
//         while (idx1 <= mid ) { 
//             merged[x++] = arr[idx1++];
//         }
//         while(idx2 <= ei){
//             merged[x++] = arr[idx2++];
//         }
// for(int i = 0, j = si; i < merged.length;i++,j++ ){
//     arr[j] = merged[i];
    
// }

//     }
//     public static void divide(int arr[] , int si , int ei){
//         if(si >= ei){
//             return;
//         }
//         int mid = si + (ei - si)/2;
//         System.out.println(si + "+" + mid);

//         divide(arr,si,mid);
//         System.out.println(mid + "+" + ei);

//         divide(arr,mid+1,ei);
//         conquer(arr,si,mid,ei);
//     }
//     public static void main(String[] args) {
//         int arr[] = {9,5,7,3,8,2};
//         int n = arr.length;
//         divide(arr, 0, n-1);
//         for(int i = 0; i < n; i++){
//         System.out.print(arr[i]);
//         }
//     }}
