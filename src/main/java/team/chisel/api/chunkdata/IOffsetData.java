package team.chisel.api.chunkdata;

/**
 * An object which contains info about the offset data for a chunk.
 */
public interface IOffsetData {

	/**
	 * Gets the X offset for this data
	 * 
	 * @return An int from 0-15
	 */
	int getOffsetX();

	/**
	 * Gets the Y offset for this data
	 * 
	 * @return An int from 0-15
	 */
	int getOffsetY();

	/**
	 * Gets the Z offset for this data
	 * 
	 * @return An int from 0-15
	 */
	int getOffsetZ();

}
