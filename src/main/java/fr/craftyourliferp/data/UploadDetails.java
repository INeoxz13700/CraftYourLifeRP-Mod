package fr.craftyourliferp.data;

public class UploadDetails {


		private boolean canUploaded = false;
		
		public String detail;
		
		public Double errorCode;
		
		public UploadDetails()
		{

		}
		
		public UploadDetails(boolean canUpload, String detail)
		{
			this.canUploaded = canUpload;
			this.detail = detail;
		}
		
		public boolean canUploaded()
		{
			return this.canUploaded;
		}
		
		public String getUploadDetail()
		{
			return detail;
		}
	
}
