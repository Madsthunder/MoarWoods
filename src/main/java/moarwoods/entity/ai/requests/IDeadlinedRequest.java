package moarwoods.entity.ai.requests;

public interface IDeadlinedRequest extends Request
{
	public long getDeadline();
	
	public boolean forgivableRequest();
}