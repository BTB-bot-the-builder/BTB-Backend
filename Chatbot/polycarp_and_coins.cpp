// polycarp_and_coins.cpp
#include<bits/stdc++.h>
using namespace std;

typedef long long ll;

int main(){
	int t;
	cin>>t;
	while(t--){
		ll n;
		cin>>n;
		ll rem = n%3;
		if(rem==0){
			cout<<n/3<<" "<<n/3<<endl;
		}
		else if(rem==1){
			cout<<n/3 + 1<<" "<<n/3<<endl;
		}
		else{
			cout<<n/3<<" "<<n/3+1<<endl;
		}
	}
}