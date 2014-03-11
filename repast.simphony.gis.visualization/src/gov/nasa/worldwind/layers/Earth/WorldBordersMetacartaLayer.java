/*
Copyright (C) 2001, 2008 United States Government
as represented by the Administrator of the
National Aeronautics and Space Administration.
All Rights Reserved.
 */
package gov.nasa.worldwind.layers.Earth;

import gov.nasa.worldwind.avlist.*;
import gov.nasa.worldwind.geom.*;
import gov.nasa.worldwind.layers.BasicTiledImageLayer;
import gov.nasa.worldwind.util.*;

import java.net.*;

/**
 * @author Patrick Murris
 * @version $Id$
 */
public class WorldBordersMetacartaLayer extends BasicTiledImageLayer
{
	public WorldBordersMetacartaLayer()
	{
		super(makeLevels(new URLBuilder()));
		this.setUseTransparentTextures(true);
	}

	private static LevelSet makeLevels(URLBuilder urlBuilder)
	{
		AVList params = new AVListImpl();

		params.setValue(AVKey.TILE_WIDTH, 512);
		params.setValue(AVKey.TILE_HEIGHT, 512);
		params.setValue(AVKey.DATA_CACHE_NAME, "Earth/WorldBorders_Metacarta");
		params.setValue(AVKey.SERVICE, "http://labs.metacarta.com/wms/vmap0");
		params.setValue(AVKey.DATASET_NAME, "country_02");
		params.setValue(AVKey.FORMAT_SUFFIX, ".dds");
		params.setValue(AVKey.NUM_LEVELS, 12);
		params.setValue(AVKey.NUM_EMPTY_LEVELS, 0);
		params.setValue(AVKey.LEVEL_ZERO_TILE_DELTA, new LatLon(Angle.fromDegrees(36d), Angle.fromDegrees(36d)));
		params.setValue(AVKey.SECTOR, Sector.FULL_SPHERE);
		params.setValue(AVKey.TILE_URL_BUILDER, urlBuilder);

		return new LevelSet(params);
	}

	private static class URLBuilder implements TileUrlBuilder
	{
		public URL getURL(Tile tile, String fileFormat) throws MalformedURLException
		{
			StringBuffer sb = new StringBuffer(tile.getLevel().getService());
			if (sb.lastIndexOf("?") != sb.length() - 1)
				sb.append("?");
			sb.append("request=GetMap");
			sb.append("&layers=");
			sb.append(tile.getLevel().getDataset());
			sb.append("&srs=EPSG:4326");
			sb.append("&width=");
			sb.append(tile.getLevel().getTileWidth());
			sb.append("&height=");
			sb.append(tile.getLevel().getTileHeight());

			Sector s = tile.getSector();
			sb.append("&bbox=");
			sb.append(s.getMinLongitude().getDegrees());
			sb.append(",");
			sb.append(s.getMinLatitude().getDegrees());
			sb.append(",");
			sb.append(s.getMaxLongitude().getDegrees());
			sb.append(",");
			sb.append(s.getMaxLatitude().getDegrees());

			sb.append("&format=image/png");
			sb.append("&version=1.1.1");
			sb.append("&transparent=true");

			return new java.net.URL(sb.toString());
		}
	}

	@Override
	public String toString()
	{
		//return Logging.getMessage("layers.Earth.WorldBorders.Name");
		return "World Borders";
	}
}
