
class ReferenceBlock extends Block {
	public ReferenceBlock(int offset, int length) {
		
	}
	
	public static ReferenceBlock fromBytes(byte[] bytes) {
		return new ReferenceBlock(0, 0);
	}
	
	@Override
	public int getFlag() {
		return 0;
	}
	
	@Override
	public byte[] getBytes() {
		return new byte[0];
	}
	
	@Override
	public void print() {
		
	}
}