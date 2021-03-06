package solvers;

import java.io.*;
import java.util.Random;

public class Cross {
	private static short[][] eom=new short[7920][6], epm=new short[11880][6];
	private static byte[] epd = new byte[11880],eod = new byte[7920];
	private static int[] ed = new int[23760];
	private static byte[][] fcm = new byte[24][6], fem = new byte[24][6];
	private static byte[][] fecd = new byte[4][576];
	private static int[] goalCo = {12, 15, 18, 21};
	private static int[] goalFeo = {0, 2, 4, 6};
	private static StringBuffer sb;
	public static boolean inic=false, inix=false, inie=false;
	private static String[] color={"D","U","L","R","F","B"};
	private static String[][] moveIdx={{"UDLRFB","DURLFB","RLUDFB","LRDUFB","BFLRUD","FBLRDU"},
		{"UDLRFB","DURLFB","RLUDFB","LRDUFB","BFRLDU","FBRLUD"},
		{"UDLRFB","DURLFB","RLUDFB","LRDUFB","BFUDRL","FBUDLR"},
		{"UDLRFB","DURLFB","RLUDFB","LRDUFB","BFDULR","FBDURL"},
		{"UDLRFB","DULRBF","RLBFUD","LRFBUD","BFLRUD","FBRLUD"},
		{"UDLRFB","DULRBF","RLFBDU","LRBFDU","BFRLDU","FBLRDU"}};
	private static String[][] rotIdx={{"","z2","z'","z","x'","x"},{"z2","","z","z'","x","x'"},
		{"z","z'","","z2","y","y'"},{"z'","z","z2","","y'","y"},
		{"x","x'","y'","y","","y2"},{"x'","x","y","y'","y2",""}};
	private static String[][] turn={{"U","D","L","R","F","B"},{"D","U","R","L","F","B"},
		{"R","L","U","D","F","B"},{"L","R","D","U","F","B"},
		{"B","F","L","R","U","D"},{"F","B","L","R","D","U"}};
	private static String[] suff={"","2","'"};
	public static void circle(int[] d, int f,int h,int l,int n,int s){
		int q=d[f];d[f]=d[n]^s;d[n]=d[l]^s;d[l]=d[h]^s;d[h]=q^s;
	}
	
	static void idxToPerm(int[] s, int p) {
		int v;
		for(int q=1;4>=q;q++){
			int t=p%q;
			for(p=p/q,v=q-2;v>=t;v--)
				s[v+1]=s[v];
			s[t]=4-q;
		}
	}
	
	static int permToIdx(int[] s) {
		int i=0, v, t;
		for(int q=0;4>q;q++){
			for(v=t=0;4>v&&!(s[v]==q);v++)if(s[v]>q)t++;
			i=i*(4-q)+t;
		}
		return i;
	}
	
	private static int idxToComb(int[] n, int[] s, int c, int o) {
		int q=4;
		for(int t=0;12>t;t++)
			if(c>=Im.Cnk[11-t][q]){
				c-=Im.Cnk[11-t][q--];
				n[t]=s[q]<<1|o&1;
				o>>=1;
			}
			else n[t]=-1;
		return o;
	}
	
	private static int getmv(int c,int p, int o,int f){
		int[] n=new int[12], s=new int[4];
		int q,t;
		idxToPerm(s, p);
		o = idxToComb(n, s, c, o);
		switch(f){
		case 0:
			circle(n,0,1,2,3,0);break;
		case 1:
			circle(n,11,10,9,8,0);break;
		case 2:
			circle(n,1,4,9,5,0);break;
		case 3:
			circle(n,3,6,11,7,0);break;
		case 4:
			circle(n,0,7,8,4,1);break;
		case 5:
			circle(n,2,5,10,6,1);break;
		}
		c=0;q=4;
		for(t=0;12>t;t++)
			if(0<=n[t]){
				c+=Im.Cnk[11-t][q--];
				s[q]=n[t]>>1;
			o|=(n[t]&1)<<3-q;
			}
		int i=permToIdx(s);
//		for(q=0;4>q;q++){
//			for(v=t=0;4>v&&!(s[v]==q);v++)if(s[v]>q)t++;
//			i=i*(4-q)+t;
//		}
		return 24*c+i<<4|o;
	}
	protected static void read(short[][] arr, InputStream in) throws IOException {
		int len = arr.length;
		byte[] buf = new byte[len * 2];
		for (int i=0; i<6; i++) {
			in.read(buf);
			for (int j=0; j<len; j++) {
				arr[j][i] = (short) (buf[j*2]&0xff | (buf[j*2+1]<<8)&0xff00);
			}
		}	
	}
	protected static void write(short[][] arr, OutputStream out) throws IOException {
		int len=arr.length;
		byte[] buf = new byte[len * 2];
		for (int i=0; i<6; i++) {
			int idx = 0;
			for (int j=0; j<len; j++) {
				buf[idx++] = (byte)(arr[j][i] & 0xff);
				buf[idx++] = (byte)((arr[j][i]>>>8) & 0xff);
			}
			out.write(buf);
		}	
	}
	private static void initc(){
		if(inic)return;
		int i,j,D,y,C;
		try {
			InputStream in = new BufferedInputStream(new FileInputStream("/data/data/com.dctimer/databases/cross.dat"));
			read(epm, in);
			read(eom, in);
			in.close();
		} catch (Exception e) {
			for(i=0;495>i;i++){
				for(j=0;24>j;j++){
					for(int s=0;6>s;s++){
						D=getmv(i,j,j,s);
						epm[24*i+j][s]=(short) (D>>4);
						if(16>j)eom[16*i+j][s]=(short) (((D>>4)/24)<<4|D&15);
					}
				}
			}
			try {
				OutputStream out = new BufferedOutputStream(new FileOutputStream("/data/data/com.dctimer/databases/cross.dat"));
				write(epm, out);
				write(eom, out);
				out.close();
			} catch (Exception e1) { }
		}
		for(i=0;11880>i;i++)epd[i]=-1;
		epd[0]=0;i=1;
		for(j=0;5>=j;j++)
			for(D=0;11880>D;D++)
				if(epd[D]==j)
					for(int s=0;6>s;s++)
						for(y=D,C=0;3>C;C++){
							y=epm[y][s];
							if(-1==epd[y]){epd[y]=(byte) (j+1);i++;}
						}
		for(i=0;7920>i;i++)eod[i]=-1;
		eod[0]=0;i=1;
		for(j=0;6>=j;j++)
			for(D=0;7920>D;D++)
				if(eod[D]==j)
					for(int s=0;6>s;s++){
						y=D;
						for(C=0;3>C;C++){
							y=eom[y][s];
							if(-1==eod[y])eod[y]=(byte) (j+1);
							i++;
						}
					}
		inic=true;
	}
	private static void initx(){
		if(inix)return;
		initc();
		byte[][] p = {
				{1,0,3,0,0,4},{2,1,1,5,1,0},{3,2,2,1,6,2},{0,3,7,3,2,3},
				{4,7,0,4,4,5},{5,4,5,6,5,1},{6,5,6,2,7,6},{7,6,4,7,3,7}
		};
		byte[][] o = {
				{0,0,1,0,0,2},{0,0,0,2,0,1},{0,0,0,1,2,0},{0,0,2,0,1,0},
				{0,0,2,0,0,1},{0,0,0,1,0,2},{0,0,0,2,1,0},{0,0,1,0,2,0}
		};
		for(int i=0; i<8; i++)
			for(int j=0; j<3; j++)
				for(int k=0; k<6; k++)
					fcm[i*3+j][k] = (byte) (p[i][k]*3+(o[i][k]+j)%3);
		p = new byte[][] {
				{0,0,7,0,0,8},{1,1,1,9,1,4},{2,2,2,5,10,2},{3,3,11,3,6,3},
				{5,4,4,4,4,0},{6,5,5,1,5,5},{7,6,6,6,2,6},{4,7,3,7,7,7},
				{8,11,8,8,8,1},{9,8,9,2,9,9},{10,9,10,10,3,10},{11,10,0,11,11,11}
		};
		o = new byte[][] {
				{0,0,0,0,0,1},{0,0,0,0,0,1},{0,0,0,0,1,0},{0,0,0,0,1,0},
				{0,0,0,0,0,1},{0,0,0,0,0,0},{0,0,0,0,1,0},{0,0,0,0,0,0},
				{0,0,0,0,0,1},{0,0,0,0,0,0},{0,0,0,0,1,0},{0,0,0,0,0,0}
		};
		for (int i = 0; i < 12; i++)
			for (int j = 0; j < 2; j++)
				for (int k = 0; k < 6; k++)
					fem[i * 2 + j][k] = (byte) (p[i][k]*2+(o[i][k]^j));
		for(int idx=0; idx<4; idx++){
			for(int i=0; i<576; i++)fecd[idx][i]=-1;
			fecd[idx][idx*51+12]=0;
			for(int d=0; d<6; d++)
				for(int i=0; i<576; i++)
					if(fecd[idx][i]==d)
						for(int j=0; j<6; j++)
							for(int y=i,k=0; k<3; k++){
								y=24*fem[y/24][j]+fcm[y%24][j];
								if(fecd[idx][y]==-1)
									fecd[idx][y]=(byte)(d+1);
							}
		}
		inix=true;
	}
	private static boolean idacross(int ep, int eo, int d,int lf,int face){
        if(0==d) return 0==ep && 0==eo;
        if(epd[ep] > d || eod[eo] > d) return false;
        int y, s, j, i;
        for(i=0; 6>i; i++)
        	if(i != lf){
        		y=ep; s=eo;
        		for(j=0; 3>j; j++) {
        			y=epm[y][i]; s=eom[s][i];
        			if(idacross(y, s, d-1, i, face)){
        				sb.insert(0, " "+turn[face][i]+suff[j]);
        				return true;
        			}
        		}
        	}
        return false;
	}
	private static boolean idaxcross(int ep, int eo, int co, int feo, int idx, int depth, int l) {
		if (depth == 0) return ep == 0 && eo == 0 && co == goalCo[idx] && feo == goalFeo[idx];
		if (epd[ep] > depth || eod[eo] > depth || fecd[idx][feo*24+co] > depth) return false;
		for (int i = 0; i < 6; i++)
			if (i != l) {
				int w = co, y = ep, s = eo, t = feo;
				for (int j = 0; j < 3; j++) {
					w = fcm[w][i]; t = fem[t][i];
					y = epm[y][i]; s = eom[s][i];
					if (idaxcross(y, s, w, t, idx, depth - 1, i)) {
						sb.insert(0, " " + turn[0][i] + suff[j]);
						return true;
					}
				}
			}
		return false;
	}
	
	public static String cross(String q2, int face, int side) {
		initc();
		String[] q=q2.split(" ");
		if(side==6){
			StringBuffer cross=new StringBuffer();
			for(int i=0;i<6;i++)cross.append(cross(q2, face, i));
			return cross.toString();
		}
		int s,D,C;
		for(s=0,D=0,C=0;C<q.length;C++)
			if(0!=q[C].length()){
				int o=moveIdx[face][side].indexOf(q[C].charAt(0));
				s=eom[s][o]; D=epm[D][o];
				if(1<q[C].length()) {
					if(q[C].charAt(1)=='2'){s=eom[s][o];D=epm[D][o];}
					else {s=eom[eom[s][o]][o];D=epm[epm[D][o]][o];}
				}
			}
		sb=new StringBuffer();
		for(C=0;9>C&&!idacross(D,s,C,-1,face);C++);
		return "\nCross("+color[side]+"): "+rotIdx[face][side]+sb.toString();
	}
	public static String solve(String s, int face) {
		String[] scr = s.split(" ");
		int[] co = new int[4], feo = new int[4];
		for (int i = 0; i < 4; i++) {co[i] = goalCo[i];feo[i] = goalFeo[i];}
		int ep = 0, eo = 0;
		for (int d = 0; d < scr.length; d++)
			if (0 != scr[d].length()) {
				int o = moveIdx[0][face].indexOf(scr[d].charAt(0));
				for (int i = 0; i < 4; i++) {co[i] = fcm[co[i]][o]; feo[i] = fem[feo[i]][o];}
				ep = epm[ep][o]; eo = eom[eo][o];
				if (1 < scr[d].length()) {
					if (scr[d].charAt(1) == '2') {
						for (int i = 0; i < 4; i++) {co[i] = fcm[co[i]][o]; feo[i] = fem[feo[i]][o];}
						eo = eom[eo][o]; ep = epm[ep][o];
					} else {
						for (int i = 0; i < 4; i++) {co[i] = fcm[fcm[co[i]][o]][o];feo[i] = fem[fem[feo[i]][o]][o];}
						eo = eom[eom[eo][o]][o]; ep = epm[epm[ep][o]][o];
					}
				}
			}
		sb = new StringBuffer();
		for (int d = 0; ; d++)
			for (int idx = 0; idx < 4; idx++)
				if (idaxcross(ep, eo, co[idx], feo[idx], idx, d, -1))
					return "\nXCross(" + color[face] + "): " + rotIdx[0][face] + sb.toString();
	}
	public static String xcross(String scr, int face) {
		initx();
		if (face < 6) return solve(scr, face);
		StringBuffer s = new StringBuffer();
		for (int i = 0; i < 6; i++) s.append(solve(scr, i));
		return s.toString();
	}
	
	private static int getPruning(int[] table, int index) {
		return table[index >> 3] >> ((index & 7) << 2) & 15;
	}
	private static void setPruning(int[] table, int index, int value) {
		table[index >> 3] ^= (15 ^ value) << ((index & 7) << 2);
	}
	public static byte[][] easyCross(int depth) {
		if(!inie) {
			initc();
			for(int i=0; i<23760; i++)ed[i]=-1;
			setPruning(ed, 0, 0);
			//int c=1;
			for(int d=0; d<8; d++) {
				//c=0;
				for(int i=0; i<190080; i++)
					if(getPruning(ed, i)==d)
						for(int s=0; s<6; s++) {
							int y = i; 
							for(int C=0; C<3; C++) {
								int ori = y&15;
								int p = epm[y>>4][s];
								int o = eom[y/384<<4|ori][s];
								y = p<<4|(o&15);
								if(getPruning(ed, y)==15) {
									setPruning(ed, y, d+1);
									//c++;
								}
							}
						}
				//System.out.println(d+" "+c);
			}
			inie = true;
		}
		Random r = new Random();
		int i;//r.nextInt(190080);
		if(depth==0)i=0;
		else for(; ; ) {
			i = r.nextInt(190080);
			if(getPruning(ed, i) <= depth)break;
		}
		int comb = i/384;
		int perm = (i>>4)%24;
		int ori = i&15;
		int[] c = new int[12];
		int[] p = new int[4];
		idxToPerm(p, perm);
		idxToComb(c, p, comb, ori);
		byte[][] arr = new byte[2][12];
		int[] idx = new int[]{7,6,5,4,10,9,8,11,3,2,1,0};
		for(i=0; i<12; i++) {
			if(c[i]==-1) arr[0][idx[i]]=arr[1][idx[i]]=-1;
			else {
				arr[0][idx[i]]=(byte) (c[i]>>1);
				arr[1][idx[i]]=(byte) (c[i]&1);
			}
		}
		return arr;
	}
}
