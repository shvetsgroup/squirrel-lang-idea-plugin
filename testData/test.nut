/*
 * This file is part of CityBuilder, which is a GameScript for OpenTTD
 *
 * CityBuilder is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation; version 2 of the License
 *
 * CityBuilder is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with CityBuilder; If not, see <http://www.gnu.org/licenses/> or
 * write to the Free Software Foundation, Inc., 51 Franklin Street,
 * Fifth Floor, Boston, MA 02110-1301 USA.
 *
 */

  // Contributors: Aphid, XfrankX

  // SendStats is an extra, optional file.
  // Multiplayer server admins can use the SendStatistics function
  // to send information via their own extensions to the script.

  // If you use the first version, do not forget to modify cargo_suffix
  // so that it matches your newGRF setup.
  // The GS version is more flexible but requires GSText support!

// void SendStatistics(CompanyID, function(x, CompanyID), function(int i))
// NOTE: second term is just the function pointer!
// Example call: SendStatistics(0, send_function), where
// send_function is the function name.

// Chatfnc is a function that accepts two parameters
// void Chatfnc (string str,CompanyID, id)
// It should do the following:
// Broadcasts str to company no. id.

// cargo_suffix_fnc is some function that accepts a cargo_ID
// and then returns the proper suffix.
// Example, cargo_suffix_fnc(1) returns "tonnes of coal"

local CityBuilder = class {}

function CityBuilder::SendStatistics(cmp, chatfnc, cargo_suffix_fnc)
{
if(0 > cmp || cmp > 31){GSLog.Error("Company number out of bounds!"); return;}
local has_goal = false;
local townid = -1;
foreach(cm in this.companies)
	{
	if(cm.id == cmp)
		{
		townid = cm.my_town;
		}
	}
if(townid >= 0)
	{
	local townname = GSTown.GetName(townid);
	local inhabitants = GSTown.GetPopulation(townid);
	local houses = GSTown.GetHouseCount(townid);
	chatfnc("-= Town Info =-", cmp);
	chatfnc("Town Name: " + townname + "", cmp);
	chatfnc("Inhabitants: " + inhabitants + "", cmp);
	chatfnc("No. of Houses: " + houses + "", cmp);
	if(this.towns[townid].is_growing)
		{chatfnc("The town is growing!", cmp);}
		else{chatfnc("The town is not growing.", cmp);}
	chatfnc(" ", cmp);
	chatfnc("-= Cargo Information =-", cmp);
	for(local i = 0; i < 32; ++i)
		{
		local cargo_goal = (this.towns[townid]).goal_cargo[i];
		if(cargo_goal > 0)
			{
			has_goal = true;
			local cargo_supply = (this.towns[townid]).supply_cargo[i];
			local cargo_stocked = (this.towns[townid]).stocked_cargo[i];
			local cargo_stocked = (this.towns[townid]).stocked_cargo[i];
			if( (this.towns[townid]).decay_rates[i] != 1000)
				{
				if(cargo_supply + cargo_stocked < cargo_goal)
					{
					chatfnc("" + cargo_supply + "/" + cargo_goal + cargo_suffix_fnc(i) + " (Still Required)" + " Storage: " +cargo_stocked + cargo_suffix_fnc(i) ,cmp);
					}
				else
					{
					chatfnc("" + cargo_supply + "/" + cargo_goal + cargo_suffix_fnc(i) + " (Delivered)" + " Storage: " +cargo_stocked + cargo_suffix_fnc(i) ,cmp);
					}
				}
			else{
				if(cargo_supply + cargo_stocked < cargo_goal)
					{
					chatfnc("" + cargo_supply + "/" + cargo_goal + cargo_suffix_fnc(i) + " (Still Required)" ,cmp);
					}
				else
					{
					chatfnc("" + cargo_supply + "/" + cargo_goal + cargo_suffix_fnc(i) + " (Delivered)" ,cmp);
					}
				}
			}
		}
		if(!has_goal){
			chatfnc("Supply a monthly transport service to start growth.", cmp)
			}
	}
	else
	{
	chatfnc("You didn't claim a (suitable) town yet. No statistics available!", cmp);
	}
}

// Same as previous function, but uses GSText objects.

function CityBuilder::SendStatisticsGS(cmp, chatfnc)
{
if(0 > cmp || cmp > 31){GSLog.Error("Company number out of bounds!");  return;}
local has_goal = false;
local townid = -1;
foreach(cm in this.companies)
	{
	if(cm.id == cmp)
		{
		townid = cm.my_town;
		}
	}
if(townid >= 0)
	{
		local inhabitants = GSTown.GetPopulation(townid);
		local houses = GSTown.GetHouseCount(townid);
		chatfnc(GSText(GSText.STR_CITYBUILDER_SS_TOWN1), cmp);
		chatfnc(GSText(GSText.STR_CITYBUILDER_SS_TOWN2_NAME, townid), cmp);
		chatfnc(GSText(GSText.STR_CITYBUILDER_SS_TOWN3_INH, inhabitants), cmp);
		chatfnc(GSText(GSText.STR_CITYBUILDER_SS_TOWN4_HC, houses), cmp);
		if(this.towns[townid].is_growing)
			{chatfnc(GSText(GSText.STR_CITYBUILDER_GROWING), cmp);}
			else{chatfnc(GSText(GSText.STR_CITYBUILDER_NOT_GROWING), cmp);}
		chatfnc(GSText(GSText.STR_CITYBUILDER_SS_TOWN5_EMPTY), cmp);
		chatfnc(GSText(GSText.STR_CITYBUILDER_SS_TOWN6_CI), cmp);
		for(local i = 0; i < 32; ++i)
			{
			local cargo_goal = (this.towns[townid]).goal_cargo[i];
			if(cargo_goal > 0)
				{
				has_goal = true;
				local cargo_supply = (this.towns[townid]).supply_cargo[i];
				local cargo_stocked = (this.towns[townid]).stocked_cargo[i];
				if( (this.towns[townid]).decay_rates[i] != 1000)
					{
					if(cargo_supply + cargo_stocked < cargo_goal)
						{
						chatfnc(GSText(GSText.STR_CITYBUILDER_SS_TOWNX_CARGOLINE, i, supply_cargo[i], i, goal_cargo[i],i, stocked_cargo[i]));
						}
					else
						{
						chatfnc(GSText(GSText.STR_CITYBUILDER_SS_TOWNX_REQLINE, i, supply_cargo[i], i, goal_cargo[i],i, stocked_cargo[i]));
						}
					}
				else
					{
					if(cargo_supply + cargo_stocked < cargo_goal)
						{
						chatfnc(GSText(GSText.STR_CITYBUILDER_SS_TOWNY_CARGOLINE, i, supply_cargo[i], i, goal_cargo[i]));
						}
					else
						{
						chatfnc(GSText(GSText.STR_CITYBUILDER_SS_TOWNY_REQLINE, i, supply_cargo[i], i, goal_cargo[i]));
						}
					}
			}
		if(!has_goal){
		chatfnc(GSText(GSText.STR_CITYBUILDER_VILLAGE), cmp)
		}
	}
	else
	{
	chatfnc(GSText(GSText.STR_CITYBUILDER_SS_NOTOWN), cmp);
	}
}
