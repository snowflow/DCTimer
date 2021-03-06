package min2phase;

import java.util.Random;
import java.io.*;

import solvers.Cross;

/**
 * Some useful functions.
 */
public class Tools {
	protected static final boolean USE_TWIST_FLIP_PRUN = false;
	public static boolean inited = false;
	private static Random gen = new Random();
	
	public static void read(char[] arr, InputStream in) throws IOException {
		int len = arr.length;
		byte[] buf = new byte[len * 2];
		in.read(buf);
		for (int i=0; i<len; i++) {
			arr[i] = (char) (buf[i*2]&0xff | (buf[i*2+1]<<8)&0xff00);
		}
	}
	
	public static void read(int[] arr, InputStream in) throws IOException {
		int len = arr.length;
		byte[] buf = new byte[len * 4];
		in.read(buf);
		for (int i=0; i<len; i++) {
			arr[i] = buf[i*4]&0xff | (buf[i*4+1]<<8)&0xff00 | (buf[i*4+2]<<16)&0xff0000 | (buf[i*4+3]<<24)&0xff000000;
		}
	}
	
	public static void read(char[][] arr, InputStream in) throws IOException {
		int len = arr[0].length;
		byte[] buf = new byte[len * 2];
		for (int i=0, leng=arr.length; i<leng; i++) {
			in.read(buf);
			for (int j=0; j<len; j++) {
				arr[i][j] = (char) (buf[j*2]&0xff | (buf[j*2+1]<<8)&0xff00);
			}
		}	
	}
	
	public static void write(char[] arr, OutputStream out) throws IOException {
		int len = arr.length;
		byte[] buf = new byte[len * 2];
		int idx = 0;
		for (int i=0; i<len; i++) {
			buf[idx++] = (byte)(arr[i] & 0xff);
			buf[idx++] = (byte)((arr[i]>>>8) & 0xff);
		}
		out.write(buf);
	}
	
	public static void write(int[] arr, OutputStream out) throws IOException {
		int len=arr.length;
		byte[] buf = new byte[len * 4];
		int idx = 0;
		for (int i=0; i<len; i++) {
			buf[idx++] = (byte)(arr[i] & 0xff);
			buf[idx++] = (byte)((arr[i]>>>8) & 0xff);
			buf[idx++] = (byte)((arr[i]>>>16) & 0xff);
			buf[idx++] = (byte)((arr[i]>>>24) & 0xff);
		}
		out.write(buf);
	}
	
	public static void write(char[][] arr, OutputStream out) throws IOException {
		int len=arr[0].length;
		byte[] buf = new byte[len * 2];
		for (int i=0, leng=arr.length; i<leng; i++) {
			int idx = 0;
			for (int j=0; j<len; j++) {
				buf[idx++] = (byte)(arr[i][j] & 0xff);
				buf[idx++] = (byte)((arr[i][j]>>>8) & 0xff);
			}
			out.write(buf);
		}
	}
	
	protected Tools() {}
	
	public synchronized static void init() {
		if (inited)
			return;
		try {
			InputStream in = new BufferedInputStream(new FileInputStream("/data/data/com.dctimer/databases/rp3.dat"));
			read(CubieCube.FlipS2R, in);
			read(CubieCube.TwistS2R, in);
			read(CubieCube.EPermS2R, in);
			read(CubieCube.MtoEPerm, in);
			read(CoordCube.TwistMove, in);
			read(CoordCube.FlipMove, in);
			read(CoordCube.UDSliceMove, in);
			read(CoordCube.UDSliceConj, in);
			read(CoordCube.CPermMove, in);
			read(CoordCube.EPermMove, in);
			read(CoordCube.MPermMove, in);
			read(CoordCube.MPermConj, in);
			read(CoordCube.UDSliceTwistPrun, in);
			read(CoordCube.UDSliceFlipPrun, in);
			read(CoordCube.MCPermPrun, in);
			read(CoordCube.MEPermPrun, in);
			if (USE_TWIST_FLIP_PRUN) {
				read(CoordCube.TwistFlipPrun, in);
			}
			in.close();
			CubieCube.initMove();
			CubieCube.initSym();
		} catch (Exception e) {
			//e.printStackTrace();
			CubieCube.initMove();
	        CubieCube.initSym();
	        CubieCube.initFlipSym2Raw();
	        CubieCube.initTwistSym2Raw();
	        CubieCube.initPermSym2Raw();

	        CoordCube.initFlipMove();
	        CoordCube.initTwistMove();
	        CoordCube.initUDSliceMoveConj();

	        CoordCube.initCPermMove();
	        CoordCube.initEPermMove();
	        CoordCube.initMPermMoveConj();

	        if (USE_TWIST_FLIP_PRUN) {
	            CoordCube.initTwistFlipPrun();
	        }
	        CoordCube.initSliceTwistPrun();
	        CoordCube.initSliceFlipPrun();
	        CoordCube.initMEPermPrun();
	        CoordCube.initMCPermPrun();
			try {
				OutputStream out = new BufferedOutputStream(new FileOutputStream("/data/data/com.dctimer/databases/rp3.dat"));
				write(CubieCube.FlipS2R, out);			//        672 Bytes
				write(CubieCube.TwistS2R, out);			// +      648 Bytes
				write(CubieCube.EPermS2R, out);			// +   5, 536 Bytes
				write(CubieCube.MtoEPerm, out);			// +  80, 640 Bytes
				write(CoordCube.TwistMove, out);		// +  11, 664 Bytes
				write(CoordCube.FlipMove, out);			// +  12, 096 Bytes
				write(CoordCube.UDSliceMove, out);		// +  17, 820 Bytes
				write(CoordCube.UDSliceConj, out);		// +   7, 920 Bytes
				write(CoordCube.CPermMove, out);		// +  99, 648 Bytes
				write(CoordCube.EPermMove, out);		// +  55, 360 Bytes
				write(CoordCube.MPermMove, out);		// +      480 Bytes
				write(CoordCube.MPermConj, out);		// +      768 Bytes
				write(CoordCube.UDSliceTwistPrun, out);	// +  80, 192 Bytes
				write(CoordCube.UDSliceFlipPrun, out);	// +  83, 160 Bytes
				write(CoordCube.MCPermPrun, out);		// +  33, 216 Bytes
				write(CoordCube.MEPermPrun, out);		// +  33, 216 Bytes
				if (USE_TWIST_FLIP_PRUN) {
					write(CoordCube.TwistFlipPrun, out);// + 435, 456 Bytes
				}
				out.close();
			} catch (Exception e1) { }
		}
		
		inited = true;
	}
    
	
	/**
	 * Set Random Source.
	 * @param gen new random source.
	 */
	public static void setRandomSource(Random gen) {
		Tools.gen = gen;
	}
	
	/**
	 * Generates a random cube.<br>
	 *
	 * The random source can be set by {@link cs.min2phase.Tools#setRandomSource(java.util.Random)}
	 *
	 * @return A random cube in the string representation. Each cube of the cube space has almost (depends on randomSource) the same probability.
	 *
	 * @see cs.min2phase.Tools#setRandomSource(java.util.Random)
	 * @see Search3.min2phase.Search#solution(java.lang.String facelets, int maxDepth, long timeOut, long timeMin, int verbose)
	 */
	public static String randomCube() {
		return randomState(STATE_RANDOM, STATE_RANDOM, STATE_RANDOM, STATE_RANDOM);
	}
	
	private static int resolveOri(byte[] arr, int base) {
		int sum = 0, idx = 0, lastUnknown = -1;
		for (int i=0; i<arr.length; i++) {
			if (arr[i] == -1) {
				arr[i] = (byte) gen.nextInt(base);
				lastUnknown = i;
			}
			sum += arr[i];
		}
		if (sum % base != 0 && lastUnknown != -1) {
			arr[lastUnknown] = (byte) ((30 + arr[lastUnknown] - sum) % base);
		}
		for (int i=0; i<arr.length-1; i++) {
			idx *= base;
			idx += arr[i];
		}
		return idx;
	}
	
	private static int countUnknown(byte[] arr) {
		if (arr == STATE_SOLVED) {
			return 0;
		}
		int cnt = 0;
		for (int i=0; i<arr.length; i++) {
			if (arr[i] == -1) {
				cnt++;
			}
		}
		return cnt;
	}
	
	private static int resolvePerm(byte[] arr, int cntU, int parity) {
		if (arr == STATE_SOLVED) {
			return 0;
		} else if (arr == STATE_RANDOM) {
			return parity == -1 ? gen.nextInt(2) : parity;
		}
		byte[] val = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};
		for (int i=0; i<arr.length; i++) {
			if (arr[i] != -1) {
				val[arr[i]] = -1;
			}
		}
		int idx = 0;
		for (int i=0; i<arr.length; i++) {
			if (val[i] != -1) {
				int j = gen.nextInt(idx + 1);
				byte temp = val[i];
				val[idx++] = val[j];
				val[j] = temp;
			}
		}
		int last = -1;
		for (idx=0; idx<arr.length && cntU>0; idx++) {
			if (arr[idx] == -1) {
				if (cntU == 2) {
					last = idx;
				}
				arr[idx] = val[--cntU];
			}
		}
		int p = Util.getNParity(Util.getNPerm(arr, arr.length), arr.length);
		if (p == 1-parity && last != -1) {
			byte temp = arr[idx-1];
			arr[idx-1] = arr[last];
			arr[last] = temp;
		}
		return p;
	}
	
	public static final byte[] STATE_RANDOM = null;
	public static final byte[] STATE_SOLVED = new byte[0];	
	
	public static String randomState(byte[] cp, byte[] co, byte[] ep, byte[] eo) {
		return randomState(cp, co, ep, eo, 0);
	}
	
	public static String randomState(byte[] cp, byte[] co, byte[] ep, byte[] eo, int rotate) {
		int parity;
		int cntUE = ep == STATE_RANDOM ? 12 : countUnknown(ep);
		int cntUC = cp == STATE_RANDOM ? 8 : countUnknown(cp);
		int cpVal, epVal;
		if (cntUE < 2) {	//ep != STATE_RANDOM
			if (ep == STATE_SOLVED) {
				epVal = parity = 0;
			} else {
				parity = resolvePerm(ep, cntUE, -1);
				epVal = Util.getNPerm(ep, 12);
			}
			if (cp == STATE_SOLVED) {
				cpVal = 0;
			} else if (cp == STATE_RANDOM) {
				do {
					cpVal = gen.nextInt(40320);
				} while (Util.getNParity(cpVal, 8) != parity);
			} else {
				resolvePerm(cp, cntUC, parity);
				cpVal = Util.getNPerm(cp, 8);			
			}
		} else {	//ep != STATE_SOLVED
			if (cp == STATE_SOLVED) {
				cpVal = parity = 0;
			} else if (cp == STATE_RANDOM) {
				cpVal = gen.nextInt(40320);
				parity = Util.getNParity(cpVal, 8);
			} else {
				parity = resolvePerm(cp, cntUC, -1);
				cpVal = Util.getNPerm(cp, 8);
			}
			if (ep == STATE_RANDOM) {
				do {
					epVal = gen.nextInt(479001600);
				} while (Util.getNParity(epVal, 12) != parity);
			} else {
				resolvePerm(ep, cntUE, parity);		
				epVal = Util.getNPerm(ep, 12);
			}
		}
		return Util.toFaceCube(new CubieCube(
			cpVal, 
			co == STATE_RANDOM ? gen.nextInt(2187) : (co == STATE_SOLVED ? 0 : resolveOri(co, 3)), 
			epVal, 
			eo == STATE_RANDOM ? gen.nextInt(2048) : (eo == STATE_SOLVED ? 0 : resolveOri(eo, 2))), rotate);
	}
	

	public static String randomLastLayer() {
		return randomState(
			new byte[]{-1, -1, -1, -1, 4, 5, 6, 7}, 
			new byte[]{-1, -1, -1, -1, 0, 0, 0, 0}, 
			new byte[]{-1, -1, -1, -1, 4, 5, 6, 7, 8, 9, 10, 11}, 
			new byte[]{-1, -1, -1, -1, 0, 0, 0, 0, 0, 0, 0, 0});
	}
	
	public static String randomPLL() {
		return randomState(
				new byte[]{-1, -1, -1, -1, 4, 5, 6, 7},
				STATE_SOLVED,
				new byte[]{-1, -1, -1, -1, 4, 5, 6, 7, 8, 9, 10, 11},
				STATE_SOLVED);
	}
	
	public static String randomLastSlot() {
		return randomState(
			new byte[]{-1, -1, -1, -1, -1, 5, 6, 7}, 
			new byte[]{-1, -1, -1, -1, -1, 0, 0, 0}, 
			new byte[]{-1, -1, -1, -1, 4, 5, 6, 7, -1, 9, 10, 11}, 
			new byte[]{-1, -1, -1, -1, 0, 0, 0, 0, -1, 0, 0, 0});
	}

	public static String randomZBLastLayer() {
		return randomState(
			new byte[]{-1, -1, -1, -1, 4, 5, 6, 7}, 
			new byte[]{-1, -1, -1, -1, 0, 0, 0, 0}, 
			new byte[]{-1, -1, -1, -1, 4, 5, 6, 7, 8, 9, 10, 11}, 
			STATE_SOLVED);
	}

	public static String randomCornerOfLastLayer() {
		return randomState(
			new byte[]{-1, -1, -1, -1, 4, 5, 6, 7}, 
			new byte[]{-1, -1, -1, -1, 0, 0, 0, 0}, 
			STATE_SOLVED, 
			STATE_SOLVED);
	}

	public static String randomEdgeOfLastLayer() {
		return randomState(
			STATE_SOLVED, 
			STATE_SOLVED, 
			new byte[]{-1, -1, -1, -1, 4, 5, 6, 7, 8, 9, 10, 11}, 
			new byte[]{-1, -1, -1, -1, 0, 0, 0, 0, 0, 0, 0, 0});
	}

	public static String randomCrossSolved() {
		return randomState(
			STATE_RANDOM, 
			STATE_RANDOM, 
			new byte[]{-1, -1, -1, -1, 4, 5, 6, 7, -1, -1, -1, -1}, 
			new byte[]{-1, -1, -1, -1, 0, 0, 0, 0, -1, -1, -1, -1});
	}
	
	public static String randomEdgeSolved() {
		return randomState(
			STATE_RANDOM, 
			STATE_RANDOM, 
			STATE_SOLVED, 
			STATE_SOLVED);
	}
	
	public static String randomCornerSolved() {
		return randomState(
			STATE_SOLVED,
			STATE_SOLVED,
			STATE_RANDOM, 
			STATE_RANDOM);
	}
	
	public static String randomEasyCross(int depth) {
		byte[][] e = Cross.easyCross(depth);
		return randomState(STATE_RANDOM,
				STATE_RANDOM,
				e[0], e[1]);
	}
	
	public static String superFlip() {
		return Util.toFaceCube(new CubieCube(0, 0, 0, 2047), 0);
	}
	
	public static String fromScramble(int[] scramble) {
        CubieCube c1 = new CubieCube();
        CubieCube c2 = new CubieCube();
        CubieCube tmp;
        for (int i = 0; i < scramble.length; i++) {
            CubieCube.CornMult(c1, CubieCube.moveCube[scramble[i]], c2);
            CubieCube.EdgeMult(c1, CubieCube.moveCube[scramble[i]], c2);
            tmp = c1; c1 = c2; c2 = tmp;
        }
        return Util.toFaceCube(c1, 0);
    }
    
    /**
	 * Check whether the cube definition string s represents a solvable cube.
	 * 
	 * @param facelets is the cube definition string , see {@link Search3.min2phase.Search#solution(java.lang.String facelets, int maxDepth, long timeOut, long timeMin, int verbose)}
	 * @return 0: Cube is solvable<br>
	 *         -1: There is not exactly one facelet of each colour<br>
	 *         -2: Not all 12 edges exist exactly once<br>
	 *         -3: Flip error: One edge has to be flipped<br>
	 *         -4: Not all 8 corners exist exactly once<br>
	 *         -5: Twist error: One corner has to be twisted<br>
	 *         -6: Parity error: Two corners or two edges have to be exchanged
	 */
	public static int verify(String facelets) {
		return new Search3().verify(facelets);
	}
}
